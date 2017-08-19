# Sample Custom XML PropertySource
This project uses an external XML configuration file `demo_config.xml`.
This is done by registering a `SpringApplicationRunListener` to read the XML 
configuration using apache commons configuration and adding the resultant 
`PropertySource` to the `Environment`.
Properties are mapped to Rich Value objects using the `@ConfigurationProperties` annotation.

Spring delegates the `getProperty()` calls to the underlying
`apache commons configuration` classes. 

The XML configuration can refer to properties defined in application.properties
and these placeholder's get expanded as expected.

## Issue 1
`environment.getProperty("demo.feature")` returns a list of `String`
with placeholders _not_ expanded.
`environment.getProperty("demo.feature{1})` returns a String and has
the placeholder expanded.

The end result of this issue is that `Configuration` value 
objects like `DemoProperties` that have configuration elements map to 
a List<String> do not have the property placeholders 
within each item in the list resolved.

Example: `demo.feature` in demo_config.xml maps to a `List<String>` in DemoProperties
```
Second feature,Some feature in ${demo.version}
```


See `DemoXmlConfigurationSource.addToEnvironment(...)` for  the issue.  

### Root Cause
Placeholders are only resolved for Strings as seen 
[here:77](https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/env/PropertySourcesPropertyResolver.java)
```
@Nullable
	protected <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
		if (this.propertySources != null) {
			for (PropertySource<?> propertySource : this.propertySources) {
				if (logger.isTraceEnabled()) {
					logger.trace("Searching for key '" + key + "' in PropertySource '" +
							propertySource.getName() + "'");
				}
				Object value = propertySource.getProperty(key);
				if (value != null) {
					if (resolveNestedPlaceholders && value instanceof String) {
						value = resolveNestedPlaceholders((String) value);
					}
					logKeyFound(key, propertySource, value);
					return convertValueIfNecessary(value, targetValueType);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Could not find key '" + key + "' in any property source");
		}
		return null;
	}
```
### Fix
One fix is to autowire an Environment in `DemoProperties.java`
Use `Environment.resolvePlaceholders(...)` to resolve each
item in the `List<String>`.

```
 public List<String> getFeature() {
        return feature
                .parallelStream()
                .map(feature -> environment.resolvePlaceholders(feature))
                .collect(Collectors.toList());
    }
```

# Issue 2
Mapping `String` to `Instant` in `DemoProperties`, an Invalid date string 
causes the following `ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [java.time.Instant]`:
```
org.springframework.validation.BindException: org.springframework.boot.bind.RelaxedDataBinder$RelaxedBeanPropertyBindingResult: 1 errors
Field error in object 'demo' on field 'endDateTime': rejected value [20080915T155300Z]; codes [typeMismatch.demo.endDateTime,typeMismatch.endDateTime,typeMismatch.java.time.Instant,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [demo.endDateTime,endDateTime]; arguments []; default message [endDateTime]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.time.Instant' for property 'endDateTime'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [java.time.Instant]]
	at org.springframework.boot.bind.PropertiesConfigurationFactory.checkForBindingErrors(PropertiesConfigurationFactory.java:359) ~[spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
	at org.springframework.boot.bind.PropertiesConfigurationFactory.doBindPropertiesToTarget(PropertiesConfigurationFactory.java:276) ~[spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]

```
This misleads the developer into thinking that the Converter
has not been registered.

The problem in this case is that the following date string
is an invalid ISO-8601 format:

```xml
<endDateTime>20080915T155300Z</endDateTime>
```
The fix is to correct the date String to:
```xml
<endDateTime>2008-09-15T15:53:00Z</endDateTime>
```


