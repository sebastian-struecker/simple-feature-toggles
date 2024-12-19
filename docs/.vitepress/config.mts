import {defineConfig} from 'vitepress'

export default defineConfig({
    base: "/simple-feature-toggles",
    title: "simple-feature-toggles",
    description: "Feature Toggles in Minutes",
    themeConfig: {
        search: {
            provider: 'local'
        },

        nav: [{text: 'Home', link: '/'}, {text: 'Guide', link: '/getting-started'}],

        sidebar: [{
            text: 'Getting Started', link: '/getting-started'
        }, {
            text: 'Configuration References', link: '/configuration-references'
        }, {
            text: 'API References', link: '/api-references'
        }],

        socialLinks: [{icon: 'github', link: 'https://github.com/sebastian-struecker/simple-feature-toggles'}]
    },
    head: [['link', {rel: 'icon', href: 'icons/favicon.ico'}]]
})
