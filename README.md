# rmm-services-server-app

RMM Services Server App

# Description

This REST API project was made as a proposed solution to an assignment provided by NinjaOne,
formerly known as NinjaRMM. This was also made as a learning experience with the following Java
technologies and libraries:

* Spring Boot - For MVC REST API
* H2 Database Engine - Quick database mockup tool
* JSON Web Token Library - For quickly including simple JWT authorization functionality

The project allows the API user to register a customer account following a specific JSON object.
Once the customer account has been registered, the user can then retrieve an OAuth 2.0 key (which
expires after an hour after creation) and use said key on all other messages to the remaining
endpoints within the API.

Once the customer has the authorization token, they can then perform the following (more information
on each API endpoint below):

* View all customers
* View their own customer profile
* View all devices
* View their devices
* Add devices to their account
* Upsert devices on their account
* Delete existing devices on their account
* View all services
* View their services
* Add services to their account
* Delete services from their account
* View billing information based off available services and devices

### Features

API project contains a few features as described within the assignment specification sheet:

* Basic JWT token generation and password encryption for API access
* Ability to register as a customer
* Ability to add devices and unique services to an account
* Ability to update and remove devices as needed
* Ability to remove services from an account
* Ability to view the total monthly bill
* In memory database utilizing the H2 database engine

# Getting Started

### Installation

This project was built using IntelliJ using the following tools and software:

* Gradle 7.3.2
* OpenJDK 17.0.1

## Usage

<details><summary>Customer Functionality</summary>
<p>

### Register a customer `/register`

In order to register a customer, the user must the user must perform a `POST` request containing a
JSON payload to the `/register` endpoint within the API.

The JSON snippet below is an example of what must be sent in order to register a new customer within
the system. The following JSON data elements must be populated or the user will not be registered:

* First Name
* User Name
* Password

Sample JSON request body to register a new customer:

```json
{
  "firstName": "Frodo",
  "middleName": "Middle",
  "lastName": "Baggins",
  "username": "frobag",
  "password": "whatismypassword"
}
```

Sample JSON response after registering a new customer:

```json
{
  "id": 1,
  "firstName": "Frodo",
  "middleName": "Middle",
  "lastName": "Baggins",
  "username": "frobag",
  "customerDevices": [],
  "customerServices": []
}
```

### Authenticate a customer `/authenticate`

In order to authenticate a customer and retrieve an Auth 2.0 token back, the user must perform
a `POST` request containing a JSON payload to the `/authenticate` endpoint within the API.

The JSON snippet below is an example of what must be sent in order to authenticate a customer within
the system.

Sample JSON request body to register a new customer:

```json
{
  "username": "frobag",
  "password": "whatismypassword"
}
```

Sample JSON response after authenticating a customer:

```json
{
  "token": "AUTH TOKEN HERE"
}
```

### Get All Customers `/customers`

In order to retrieve a list of all customers, the user must perform a `GET` request to
the `/customers` endpoint within the API.

### Get Customer By Customer ID `/customers/{customerID}`

In order to retrieve a specific customer, the user must perform a `GET` request containing the
customer ID within the `{customerID}` part of the `/customers/{customerID}` endpoint within the API.

Sample JSON response after retrieving the customer:

```json
{
  "id": 1,
  "firstName": "Frodo",
  "middleName": "Middle",
  "lastName": "Baggins",
  "username": "frobag",
  "customerDevices": [],
  "customerServices": [],
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1"
    },
    "customers": {
      "href": "http://localhost:8080/customers"
    }
  }
}
```

### Get Customer Bill `/customers/{customerID}/billing`

In order to retrieve the monthly cost of services and devices for a specific customer, the user must
the user must perform a `GET` request containing the customer ID within the `{customerID}` part of
the `/customers/{customerID}/billing`
endpoint within the API.

Sample JSON response after retrieving the customer bill:

```json
{
  "windowsDeviceCount": 2,
  "macDeviceCount": 3,
  "totalCost": 71.00
}
```

</p>
</details>

<details><summary>Customer Device Functionality</summary>
<p>

### Get All Devices `/customers/devices`

In order to retrieve a list of all devices, the user must perform a `GET` request to
the `/customers/devices` endpoint within the API.

### Get All Devices Under Customer ID `/customers/{customerID}/devices`

In order to retrieve all devices by a specific customer, the user must perform a `GET` request
containing the customer ID within the `{customerID}` part of the `/customers/{customerID}/devices`
endpoint within the API.

Sample JSON response after retrieving the customer devices:

```json
{
  "_embedded": {
    "customerDeviceList": [
      {
        "id": 10001,
        "systemName": "Windows Machine",
        "deviceType": "WINDOWS_WORKSTATION",
        "_links": {
          "self": {
            "href": "http://localhost:8080/customers/1/devices/10001"
          },
          "customerDevices": {
            "href": "http://localhost:8080/customers/devices"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/devices"
    }
  }
}
```

### Get Specific Device Under Customer ID `/customers/{customerID}/devices/{deviceID}`

In order to retrieve a specific device by a specific customer, the user must perform a `GET`
request containing the customer ID and device ID within the `{customerID}` and `{deviceID}` part of
the `/customers/{customerID}/devices/{deviceID}`
endpoint within the API.

Sample JSON response after retrieving the specific customer device:

```json
{
  "id": 10004,
  "systemName": "MacBook Pro",
  "deviceType": "MAC",
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1/devices/10004"
    },
    "customerDevices": {
      "href": "http://localhost:8080/customers/devices"
    }
  }
}
```

### Create New Device Under Customer ID `/customers/{customerID}/devices`

In order to create a specific device by a specific customer, the user must perform a `POST`
request containing a JSON payload and the customer ID within the `{customerID}` part of
the `/customers/{customerID}/devices`
endpoint within the API.

The `deviceType` JSON data element has a total of 3 valid values. If any value other than the ones
specified below are entered, no service will be created:

* **WINDOWS_WORKSTATION**
* **WINDOWS_SERVER**
* **MAC**

Sample JSON request body to register a new customer:

```json
{
  "id": 10004,
  "systemName": "MacBook Pro",
  "deviceType": "MAC"
}
```

Sample JSON response after retrieving the specific customer device:

```json
{
  "id": 10004,
  "systemName": "MacBook Pro",
  "deviceType": "MAC",
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1/devices/10004"
    },
    "customerDevices": {
      "href": "http://localhost:8080/customers/devices"
    }
  }
}
```

### Upsert Existing/New Device Under Customer ID `/customers/{customerID}/devices/{deviceID}`

In order to upsert a specific device by a specific customer, the user must perform a `PUT`
request containing a JSON payload and the customer ID and device ID within the `{customerID}`
and `{deviceID}` part of the `/customers/{customerID}/devices/{deviceID}`
endpoint within the API. If the item does not exist within the device ID provided, a new device will
be registered to the user.

The `deviceType` JSON data element has a total of 3 valid values. If any value other than the ones
specified below are entered, no service will be upserted:

* **WINDOWS_WORKSTATION**
* **WINDOWS_SERVER**
* **MAC**

Sample JSON request body to register a new customer:

```json
{
  "systemName": "MacBook Pro New2",
  "deviceType": "MAC"
}
```

Sample JSON response after retrieving the specific customer device:

```json
{
  "id": 10004,
  "systemName": "MacBook Pro New2",
  "deviceType": "MAC",
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1/devices/10004"
    },
    "customerDevices": {
      "href": "http://localhost:8080/customers/devices"
    }
  }
}
```

### Delete Specific Device Under Customer ID `/customers/{customerID}/devices/{deviceID}`

In order to delete a specific device by a specific customer, the user must perform a `DELETE`
request containing the customer ID and device ID within the `{customerID}` and `{deviceID}` part of
the `/customers/{customerID}/devices/{deviceID}`
endpoint within the API. A `200` OK status message will be returned if deletion was successful. If
not, a `4XX` error will be returned.

</p>
</details>

<details><summary>Customer Service Functionality</summary>
<p>

### Get All Services `/customers/services`

In order to retrieve a list of all services, the user must perform a `GET` request to
the `/customers/services` endpoint within the API.

### Get All Service Under Customer ID `/customers/{customerID}/services`

In order to retrieve all services by a specific customer, the user must perform a `GET` request
containing the customer ID within the `{customerID}` part of the `/customers/{customerID}/services`
endpoint within the API.

Sample JSON response after retrieving the customer services:

```json
{
  "_embedded": {
    "customerServiceList": [
      {
        "id": 1,
        "serviceName": "ANTIVIRUS",
        "serviceType": "ANTIVIRUS",
        "_links": {
          "self": {
            "href": "http://localhost:8080/customers/1/services/1"
          },
          "customerServices": {
            "href": "http://localhost:8080/customers/services"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/services"
    }
  }
}
```

### Get Specific Service Under Customer ID `/customers/{customerID}/services/{serviceID}`

In order to retrieve a specific service by a specific customer, the user must perform a `GET`
request containing the customer ID and service ID within the `{customerID}` and `{serviceID}` part
of the `/customers/{customerID}/services/{serviceID}`
endpoint within the API.

Sample JSON response after retrieving the specific customer service:

```json
{
  "id": 1,
  "serviceName": "PSA",
  "serviceType": "PSA",
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1/services/1"
    },
    "customerServices": {
      "href": "http://localhost:8080/customers/services"
    }
  }
}
```

### Create New Service Under Customer ID `/customers/{customerID}/services`

In order to create a specific service by a specific customer, the user must perform a `POST`
request containing a JSON payload and the customer ID within the `{customerID}` part of
the `/customers/{customerID}/service`
endpoint within the API.

The `serviceType` JSON data element has a total of 4 valid values. If any value other than the ones
specified below are entered, no service will be created:

* **ANTIVIRUS**
* **CLOUDBERRY**
* **PSA**
* **TEAMVIEWER**

> **NOTE:** Only one (1) service of each `serviceType` can be added to EACH customer, meaning a
> specific customer ID can have up to a maximum of 4 unique service types on their account.
> Attempting to add more than one (1) service type to a specific customer ID will result in service
> creation failure.

Sample JSON request body to register a new customer:

```json
{
  "serviceName": "PSA",
  "serviceType": "PSA"
}
```

Sample JSON response after retrieving the specific customer service:

```json
{
  "id": 1,
  "serviceName": "PSA",
  "serviceType": "PSA",
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1/services/1"
    },
    "customerServices": {
      "href": "http://localhost:8080/customers/services"
    }
  }
}
```

### Delete Specific Service Under Customer ID `/customers/{customerID}/services/{serviceType}`

In order to delete a specific service by a specific customer, the user must perform a `DELETE`
request containing the customer ID and service type within the `{customerID}` and `{serviceType}`
part of the `/customers/{customerID}/services/{serviceType}`
endpoint within the API. A `200` OK status message will be returned if deletion was successful. If
not, a `4XX` error will be returned.

The `serviceType` URL parameter has a total of 4 valid values. If any value other than the ones
specified below are entered, no service will be deleted:

* **ANTIVIRUS**
* **CLOUDBERRY**
* **PSA**
* **TEAMVIEWER**

</p>
</details>

<details><summary>Service Cost Functionality</summary>
<p>

### Get All Service Costs `/services`

In order to retrieve a list of all service costs, the user must perform a `GET` request to
the `/services` endpoint within the API.

```json
{
  "_embedded": {
    "serviceCostList": [
      {
        "id": 1,
        "serviceName": "Windows Antivirus",
        "serviceType": "ANTIVIRUS",
        "deviceOperatingSystem": "WINDOWS",
        "servicePrice": 5.00,
        "_links": {
          "self": {
            "href": "http://localhost:8080/services/1"
          },
          "serviceCosts": {
            "href": "http://localhost:8080/services"
          }
        }
      },
      {
        "id": 5,
        "serviceName": "Mac Antivirus",
        "serviceType": "ANTIVIRUS",
        "deviceOperatingSystem": "MAC",
        "servicePrice": 7.00,
        "_links": {
          "self": {
            "href": "http://localhost:8080/services/5"
          },
          "serviceCosts": {
            "href": "http://localhost:8080/services"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/services"
    }
  }
}
```

</p>
</details>

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.2/gradle-plugin/reference/html/)
* [Spring Boot REST API Tutorial](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)