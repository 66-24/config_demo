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

I am not sure if I am doing something wrong or if its a bug in
`Environment.getProperty(...)` specifically when a List is returned.