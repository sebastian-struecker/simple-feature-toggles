---
# https://vitepress.dev/reference/default-theme-home-page
layout: home

hero:
  name: "simple-feature-toggles"
  text: "Easy to integrate Feature Toggles for your applications"
  tagline: Ready to use in minutes
  image:
    src: icons/logo.png
    alt: simple-feature-toggles
  actions:
    - theme: brand
      text: Quickstart
      link: /getting-started
    - theme: alt
      text: Configuration
      link: /configuration-references

features:
  - icon:
      src: icons/rocket-svgrepo.svg
    title: Fast launch
    details: With easily configurable, ready-to-launch Docker images you can start using Feature Toggles in no time.
  - icon:
      src: icons/padlock-svgrepo.svg
    title: Secured
    details: The management of Feature Toggles is secured by using OpenID Connect Bearer token authentication.
  - icon:
      src: icons/lightning-svgrepo.svg
    title: Lightning fast
    details: Using Quarkus, Reactive Frameworks, and a scalable design this service supports handling many user requests.
---
