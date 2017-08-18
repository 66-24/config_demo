# Sample Custom XML PropertySource
This project uses an external XML configuration file `demo_config.xml`.
This is done by registering a `Spring Run Listener` to read the XML 
configuration using apache commons configuration and adding the resultant 
`PropertySource` to the `Environment`.
Properties are mapped to Rich Value objects using the `@ConfigurationProperties` annotation.

Spring delegates the `getProperty()` calls to the underlying
`apache commons configuration` classes. 

The XML configuration can refer to properties defined in application.properties
and these placeholder's get expanded as expected.

## Issue
`environment.getProperty("demo.feature")` returns a list of `String`
with placeholders _not_ expanded.
`environment.getProperty("demo.feature{1})` returns a String and has
the placeholder expanded.

The end result of this issue is that `Configuration` value 
objects like `DemoProperties` are mapped to a List<String>
with the property placeholders in the Strings not being
resolved.

Example: List of Strings representation of `demo.feature`
`Second feature,Some feature in ${demo.version}`


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
entry in the list of Strings.

```
 public List<String> getFeature() {
        return feature
                .parallelStream()
                .map(feature -> environment.resolvePlaceholders(feature))
                .collect(Collectors.toList());
    }
```