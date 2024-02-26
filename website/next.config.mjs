import nextra from 'nextra'



const withNextra = nextra({
  theme: 'nextra-theme-docs',
  themeConfig: './theme.config.jsx'
})

/** @type {import('next').NextConfig} */
export default withNextra({
    i18n: {
    locales: ['default', 'en', 'ru'],
    defaultLocale: 'default',
    localeDetection: false,
  },
  trailingSlash: true,
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'cdn.discordapp.com',
        port: '',
        pathname: '/attachments/**',
      },
    ],
  },
  async redirects() {
    return [
      {
        source: '/en/contest-docs/:path*',
        destination: '/ru/contest-docs/:path*',
        permanent: false,
      },
    ]
  }
})
