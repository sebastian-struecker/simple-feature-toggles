import {defineConfig} from 'vitepress'

export default defineConfig({
    base: "/simple-feature-toggles",
    title: "simple-feature-toggles",
    description: "Feature Toggles in Minutes",
    themeConfig: {
        search: {
            provider: 'local'
        },

        nav: [{text: 'Home', link: '/'}, {text: 'Guide', link: '/getting-started/service'}],

        sidebar: [{
            text: 'Getting Started', items: [{text: 'Service', link: '/getting-started/service'}, {
                text: 'API Client', link: '/getting-started/api-client'
            }, {text: 'UI', link: '/getting-started/ui'}]
        }, {
            text: 'Configuration References', link: '/configuration-references'
        }, {
            text: 'API References', link: '/api-references'
        }, {
            text: 'Test', link: '/test'
        }],

        socialLinks: [{icon: 'github', link: 'https://github.com/sebastian-struecker/simple-feature-toggles'}]
    },
    head: [['link', {rel: 'icon', href: 'icons/favicon.ico'}]]
})
