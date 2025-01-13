import DefaultTheme from 'vitepress/theme'
import VersionSwitcher from 'vitepress-versioning-plugin/src/components/VersionSwitcher.vue'
import './custom.css'

/** @type {import('vitepress').Theme} */
export default {
    extends: DefaultTheme, enhanceApp({app}) {
        app.component('VersionSwitcher', VersionSwitcher)
    }
}

