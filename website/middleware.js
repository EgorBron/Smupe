import { withLocales } from 'nextra/locales'
import { NextRequest, NextResponse } from 'next/server'

const PUBLIC_FILE = /\.(.*)$/

// export const middleware = (request => {
//     if (
//         !request.nextUrl.pathname.startsWith('/docs') ||
//         request.nextUrl.pathname.startsWith('/_next') ||
//         request.nextUrl.pathname.includes('/api/') ||
//         PUBLIC_FILE.test(request.nextUrl.pathname)
//       ) {
//         return
//       }
     
//       if (request.nextUrl.locale === 'default') {
//         const locale = request.cookies.get('NEXT_LOCALE')?.value || 'en'
     
//         return NextResponse.redirect(
//           new URL(`/${locale}${request.nextUrl.pathname}${request.nextUrl.search}`, request.url)
//         )
//       }
// })

export const middleware = withLocales( req => {
console.log(req.nextUrl.pathname)
  if (
    req.nextUrl.pathname.startsWith('/_next') ||
    req.nextUrl.pathname.includes('/api/')
  ) {
    return
  }
})

// export const config = {
//   matcher: [
//     // Skip all internal paths (_next)
//     '/((?!_next).*)',
//     // Optional: only run on root (/) URL
//     // '/'
//   ],
// }