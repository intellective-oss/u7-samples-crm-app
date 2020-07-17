&larr; [Previous step: Implementing the master-detail search template tab](./step3-implementing-master-detail.md)

On this step we are going to create a data service that will implement a set of necessary operations to work with
data sets os US states, cities, and area codes:
  * https://github.com/jasonong/List-of-US-States/blob/master/states.csv
  * https://github.com/ravisorg/Area-Code-Geolocation-Database/blob/master/us-area-code-cities.csv

Project template provides a `custom-services` modules allowing adding any server-side code using Spring 4 including
general beans and MVC capabilities. 

# Implementing data service
At first, we should introduce model objects and appropriate interface declaring all the operations we need.


&rarr; [Next step: Implementing custom selectors and criteria field validation](./step5-selectors-and-validation.md)