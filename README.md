# Covid19TraceApp

As you well know, Covid-19 remains a very challenging disease worldwide. Thus far,
there are about 4.5 million cases worldwide and about 294K deaths from this Coronavirus
pandemic. Contact tracing is one of many possible techniques that can be employed
to limit the widespread of the virus.
Traditionally, contact tracing was carried out by having patients complete paper forms,
leading thus to considerable inefficiencies caused by the inability of the patient to fully
remember the people that he/she was in contact with over the period of incubation of
the virus, which is estimated to be between 2 and 14 days (and sometimes more). So,
a patient with poor memory might influence negatively the whole process of contact
tracing, leaving many people at the risk of being infected by the virus.
The main objective of this project is to address the above problem by automating the
contact tracing process. This is particularly needed nowadays since a more accurate
contact tracing translates into faster identification of people at risk, resulting thus in a
prevention of further spread of the virus locally. It is network-based application that can help with the
early detection of Corona virus cases in a bid to reduce the risk of a widespread of the
virus, and better protect our families and loved ones. 

This project relies on iBeacon technology in detecting nearby devices and saving this information for 14 days. This information is stored in a "SQL" database.
The interaction between the application and the server is done using a PHP API. The user is asked to register by entering his/her full name and phone number for safety and notifying purposes, since our project will also have a messaging gateway in the near future.
 In this way, If the application user is diagnosed
with Covid-19, his/her recent contacts are notified and advised to either self-isolate or
seek medical help. Technically, people who are found to have been in close contact
(within a distance of 5 m) with the patient are red-flagged and their respective contacts
are also considered as potential Covid-19 cases as well. 
This was done with the help of the following repository: https://github.com/helloworldkr/Bluetooth-ble-beamer-and-scanner-for-tracing-corona-virus-infected-individual
