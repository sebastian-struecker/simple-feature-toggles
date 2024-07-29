import {defineConfig} from 'vitepress'

export default defineConfig({
    base: "/simple-feature-toggles", title: "simple-feature-toggles", description: "Feature Toggles in Minutes", themeConfig: {
        nav: [{text: 'Home', link: '/'}, {text: 'Guide', link: '/guide'}],

        sidebar: {
            '/guide/': [{
                text: 'Getting Started',
                link: '/guide/getting-started/',
                items: [
                    {text: 'Service', link: '/guide/getting-started/service'},
                    {text: 'API Client', link: '/guide/getting-started/api-client'},
                    {text: 'UI', link: '/guide/getting-started/ui'},
                ]
            }, {
                text: 'Examples', link: '/guide/examples/', items: [{
                    text: 'Keycloak Setup', link: '/guide/examples/keycloak-setup'
                }]
            }]
        },

        socialLinks: [{icon: 'github', link: 'https://github.com/sebastian-struecker/simple-feature-toggles'}]
    },
    head: [
        ['link', { rel: 'icon', href: 'icons/favicon.ico' }]
    ]
})
