export default {
  darkMode: true,
  primaryHue: 285,
  logo: <>
  <img src="/smupe_icon.svg" width="32" height="32" style={{borderRadius: '10rem', marginInline: '2rem'}}/>
  <span>Smupe! Docs</span>
  </>,
  project: {
    link: 'https://github.com/EgorBron/Smupe'
  },
  docsRepositoryBase: "https://github.com/EgorBron/Smupe/blob/master/website/pages/docs",
  navs: [],
  useNextSeoProps() {
    return {
      titleTemplate: '%s – Smupe! Docs'
    }
  },
  i18n: [
  { locale: 'en', text: 'English' },
  { locale: 'ru', text: 'Русский' },
  ]
}
