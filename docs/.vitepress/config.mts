import defineVersionedConfig from "vitepress-versioning-plugin";

export default defineVersionedConfig({
    base: "/simple-feature-toggles", title: "simple-feature-toggles",

    description: "Feature-Toggles in minutes", appearance: {
        initialValue: 'light',
    }, themeConfig: {
        search: {
            provider: 'local'
        }, // Version Switcher from: https://vvp.imb11.dev/
        versionSwitcher: false,

        nav: [{text: 'Home', link: '/'}, {text: 'References', link: '/references'}],

        sidebar: {
            '/': [{
                text: 'Quickstart', link: 'quickstart'
            }, {
                text: 'References', link: 'references'
            }, {
                text: 'Service', link: 'service', items: [{
                    text: 'Api References', link: 'service/api-references'
                },{
                    text: 'Configuration', link: 'service/configuration'
                }, {
                    text: 'Role-based access control', link: 'service/role-based-access-control'
                }, {
                    text: 'Run', collapsed: true, items: [{
                        text: 'Docker Local', link: 'service/run/docker-local'
                    }]
                }]
            }, {
                text: 'UI', link: 'ui', items: [{
                    text: 'Configuration', link: 'ui/configuration'
                }, {
                    text: 'Role-based access control', link: 'ui/role-based-access-control'
                }, {
                    text: 'Run', collapsed: true, items: [{
                        text: 'Docker Local', link: 'ui/run/docker-local'
                    }]
                }]
            }, {
                text: 'Infrastructure', link: 'infrastructure', items: [{
                    text: 'OIDC', collapsed: true, items: [{
                        text: 'Keycloak', link: 'infrastructure/oidc/keycloak'
                    }]
                }, {
                    text: 'Database', collapsed: true, items: [{
                        text: 'PostgreSQL', link: 'infrastructure/database/postgresql'
                    }]
                }]
            }]
        },

        socialLinks: [{icon: 'github', link: 'https://github.com/sebastian-struecker/simple-feature-toggles'}]
    }, head: [['link', {rel: 'icon', href: 'icons/favicon.ico'}]],

    versioning: {
        latestVersion: "1.0.0",
    },
}, __dirname)
