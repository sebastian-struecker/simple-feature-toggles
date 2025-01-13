# References

[[toc]]

## What is simple-feature-toggles?

Simple-feature-toggles is an addition to your infrastructure that enables the management and usage of feature toggles.
Because this is a container as a service solution you have full control over the technical aspects of the deployment
compared to SaaS solutions.

### Comparison

|                              | simple-feature-toggles | SaaS solutions | 
|------------------------------|------------------------|----------------|
| Manage Environments          | ✔                      | ✔              |
| Manage Api keys              | ✔                      | ✔              |
| Manage feature toggles       | ✔                      | ✔              |
| In control of infrastructure | ✔                      | ✖              |
| In control of deployment     | ✔                      | ✖              |
| More features?               | ✖                      | ✔              |

[//]: # (https://fsymbols.com/signs/tick/)

## Usage

By design simple-feature-toggles offers two Docker images, which are ready to be used.

- Service image
- UI image

The service image is the main image, which enables the management and usage of feature toggles.
The ui image will provide an interactive app to visualize your configurations.

While the ui is not required it is still recommended to use both the service and ui.

## Role-Based Access Control

Simple-feature-toggles is using the following user roles: `sft_admin`, `sft_viewer` for role-based access control.
In short, the **sft_admin** can read, write and delete everything, while the **sft_viewer** can only read certain
elements.

More details for the
roles: [Service RBAC](../service/role-based-access-control), [UI RBAC](../ui/role-based-access-control).

## Further reading

- [Service](../service/index)
- [UI](../ui/index)
- [Infrastructure](../infrastructure/index)
