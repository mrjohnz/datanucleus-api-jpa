# datanucleus-api-jpa

Support for DataNucleus persistence using the JPA API (JSR0220, JSR0317, JSR0338).  
JPA "persistence provider" is _org.datanucleus.api.jpa.PersistenceProviderImpl_.  
JPA EntityManagerFactory is implemented by _org.datanucleus.api.jpa.JPAEntityManagerFactory_.  
JPA EntityManager is implemented by _org.datanucleus.api.jpa.JPAEntityManager_.  
JPA Query is implemented by _org.datanucleus.api.jpa.JPAQuery_.  
JPA EntityTransaction is implemented by _org.datanucleus.api.jpa.JPAEntityTransaction_.  

This is built using Maven, by executing `mvn clean install` which installs the built jar in your local Maven repository.


## KeyFacts

__License__ : Apache 2 licensed  
__Issue Tracker__ : http://github.com/datanucleus/datanucleus-api-jpa/issues  
__Javadocs__ : [5.1](http://www.datanucleus.org/javadocs/api.jpa/5.1/), [5.0](http://www.datanucleus.org/javadocs/api.jpa/5.0/), [4.1](http://www.datanucleus.org/javadocs/api.jpa/4.1/), [4.0](http://www.datanucleus.org/javadocs/api.jpa/4.0/)  
__Download(Releases)__ : [Maven Central](http://central.maven.org/maven2/org/datanucleus/datanucleus-api-jpa)  
__Download(Nightly)__ : [Nightly Builds](http://www.datanucleus.org/downloads/maven2-nightly/org/datanucleus/datanucleus-api-jpa)  
__Dependencies__ : See file [pom.xml](pom.xml)  
__Support__ : [DataNucleus Support Page](http://www.datanucleus.org/support.html)  



## JPA 2.2 Status

The latest JPA spec is currently v2.1 and the JPA "expert group" is seemingly dead, yet they provide an issue tracker for people to raise issues of what they would like to see in 
the next release of JPA (if it ever happens). 
DataNucleus, not being content to wait for hell to freeze over, has gone ahead and worked on the following issues

[JPA_SPEC-25](https://github.com/javaee/jpa-spec/issues/25) : access the JPQL string query and native query from a JPA Query object. Done  
[JPA_SPEC-30](https://github.com/javaee/jpa-spec/issues/30) : case sensitive JPQL queries. _Not implemented but would simply need JDOQL semantics copying_  
[JPA_SPEC-35](https://github.com/javaee/jpa-spec/issues/35) : support for more types. DataNucleus has provided way more types since JPA1 days!  
[JPA_SPEC-41](https://github.com/javaee/jpa-spec/issues/41) : targetClass attribute for @Embedded. Provided in javax.persistence-2.2.jar  
[JPA_SPEC-42](https://github.com/javaee/jpa-spec/issues/42) : allow null embedded objects. Provided by extension  
[JPA_SPEC-48](https://github.com/javaee/jpa-spec/issues/48) : @CurrentUser on a field. _Not implemented but the same idea applies to DataNucleus multitenancy support_  
[JPA_SPEC-49](https://github.com/javaee/jpa-spec/issues/49) : @CreateTimestamp, @UpdateTimestamp. Implemented using DN extension annotations  
[JPA_SPEC-52](https://github.com/javaee/jpa-spec/issues/52) : EM.createNativeQuery(String,Class). DN already works like that  
[JPA_SPEC-63](https://github.com/javaee/jpa-spec/issues/63) : Support java.time. Implemented fully as far as we can see  
[JPA_SPEC-74](https://github.com/javaee/jpa-spec/issues/74) : Method of obtaining @Version value. Available via NucleusJPAHelper  
[JPA_SPEC-76](https://github.com/javaee/jpa-spec/issues/76) : Allow specification of null handling in ORDER BY. Provided in javax.persistence-2.2.jar for Criteria and also in JPQL string-based  
[JPA_SPEC-77](https://github.com/javaee/jpa-spec/issues/77) : EMF should implement AutoCloseable. Done  
[JPA_SPEC-85](https://github.com/javaee/jpa-spec/issues/85) : Metamodel methods to get entity by name. Provided in javax.persistence-2.2.jar  
[JPA_SPEC-86](https://github.com/javaee/jpa-spec/issues/86) : Allow multiple level inheritance strategy. Not explicitly done but we do this for JDO so likely mostly there  
[JPA_SPEC-100](https://github.com/javaee/jpa-spec/issues/100) : Allow an empty collection_valued_input_parameter in an "IN" expression. Already works like this  
[JPA_SPEC-102](https://github.com/javaee/jpa-spec/issues/102) : JPQL : DATE/TIME functions. Done, with Criteria changes in javax.persistence-2.2.jar  
[JPA_SPEC-103](https://github.com/javaee/jpa-spec/issues/103) : JPQL : Allow use of parameter in more than just WHERE/HAVING. Done  
[JPA_SPEC-105](https://github.com/javaee/jpa-spec/issues/105) : Allow AttributeConverter to multiple columns. Done but using vendor extension  
[JPA_SPEC-107](https://github.com/javaee/jpa-spec/issues/107) : JPQL : support subqueries in SELECT. Done  
[JPA_SPEC-112](https://github.com/javaee/jpa-spec/issues/112) : EntityGraph generic type incorrect. Fixed in javax.persistence-2.2.jar  
[JPA_SPEC-113](https://github.com/javaee/jpa-spec/issues/113) : Allow @GeneratedValue on non-PK fields. Provided since JPA 1  
[JPA_SPEC-115](https://github.com/javaee/jpa-spec/issues/115) : Add @Repeatable to annotations. Done in javax.persistence-2.2.jar  
[JPA_SPEC-128](https://github.com/javaee/jpa-spec/issues/128) : JPQL : Support JOIN on 2 root candidates. Done  
[JPA_SPEC-133](https://github.com/javaee/jpa-spec/issues/133) : Make GeneratedValue strategy=TABLE value available in PrePersist. Done  
[JPA_SPEC-137](https://github.com/javaee/jpa-spec/issues/137) : Add methods taking List to Criteria. Done in javax.persistence-2.2.jar  
