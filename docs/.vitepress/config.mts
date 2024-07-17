import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  base: "/insta-toggles",
  title: "insta-toggles",
  description: "Feature Toggles in Minutes",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Guide', link: '/guide' }
    ],

    sidebar: {
      '/guide/': [
        {
          text: 'Introduction',
          items: [
            { text: 'Getting Started', link: '/guide/getting-started' },
          ]
        },
        {
          text: 'Integration',
          items: [
            { text: 'Authorization Concept', link: '/guide/authorization-concept' },
            { text: 'Management Client', link: '/guide/management-client' },
            { text: 'Application Client', link: '/guide/application-client' }
          ]
        }
      ]
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/sebastian-struecker/insta-toggles' }
    ]
  }
})
