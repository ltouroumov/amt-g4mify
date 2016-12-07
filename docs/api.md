# G4mify &mdash; REST API

This document provides a usage guide for the REST API. The swagger auto-generated documentation is available at `/swagger/ui.html`.

## Obtaining access

To obtain access you must create a `Domain` which grants you access to you own pool of resources. (see [Models](models.md)).

To create a domain call the `POST /register` endpoint. The server will return a JSON object providing you with a `DomainID` and `DomainKey` which are required for authentication (don't lose them).

```
-- Request --
GET /register

-- Response --
{
   "id": 1234,
   "name": "mydomain",
   "key": "secret"
}
```

## Authenticating

To authenticate to the API, you must provide a header named `Identity` with the value of `<domain.id>:<domain.key>`.

```
-- Request --
GET /api/domain
Identiy: 1234:secret

-- Response --
{
    "name": "mydomain"
}
```
