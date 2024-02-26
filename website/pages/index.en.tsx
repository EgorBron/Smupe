import Image from "next/image"


export function Wave() {
  return (<div className="wave" style={{minWidth: '100%', padding: '4rem',}}></div>);
}

export function SmupeLogo() {
  return (<a href="/" className="flex flex-row items-center">
  <span style={{
  fontWeight: '700',
  paddingRight: '1rem'
}}>Smupe!</span>
  <Image style={{borderRadius: '10rem', overflow: 'scroll'}} src="/smupe_icon.svg" alt="logo" width={32} height={32}/>
</a>);
}

export default function Home() { return (<>
<header className="flex flex-row justify-between p-48 pt-4">
      <SmupeLogo/>
      <div className="flex flex-row items-center">
        <a href="/" className="p-2">Home</a>
        <a href="/" className="p-2">Source</a>
        <a href="/" className="p-2">Download</a>
        <a href="/docs" className="p-2">Documentation</a>
        <a href="/" className="p-2">Privacy policy</a>
      </div>
    </header>
    <main className="">
      <div className="flex flex-col p-48 pt-8 pb-8">
        <div className="flex flex-row justify-between max-w-full material-card">
          <div className="flex flex-col">
            <h1 className="main-header">
              And see your pictures, eternally
            </h1>
            <span style={{
              fontSize: '1.5rem',
              marginBottom: '2rem',
              maxWidth: '25rem',
            }}>
              Smupe! lets you watch an endless stream of images you like.
            </span>
            <div className="flex flex-row justify-stretch">
              {/*<Button variant="filled" href="/">Download</Button>
              <Button variant="outlined" href="/">More info</Button>*/}
            </div>
          </div>
          <div>
            <Image  style={{borderRadius: '1rem', overflow: 'scroll'}} src="/smupe_icon.svg" alt="logo" width={128} height={128} />
          </div>
        </div>
      </div>
      <Wave/>
      <div className="flex flex-col justify-center p-48 pt-0 pb-0">
        <div className="flex flex-row justify-between max-w-full material-card">
          <div className="flex flex-col justify-center">
            <h2>
              Just swipe and enjoy
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Smupe! lets you easily watch images from almost everywhere.
              </span>
            </div>
            <Image src="/next.svg" alt="" width={128} height={128} />
        </div>
      </div>
      <div className="flex flex-col justify-center p-48">
        <div className="flex flex-row justify-between max-w-full material-card">
            <Image src="/next.svg" alt="" width={128} height={128} />
            <div className="flex flex-col justify-center">
            <h2>
              Flexible as it can
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                You see only you want. Fast and simple image sources list, easy to use and configure.
              </span>
            </div>
        </div>
      </div>
      <div className="flex flex-col justify-center p-48 pt-0 pb-0">
        <div className="flex flex-row justify-between max-w-full material-card">
          <div className="flex flex-col justify-center">
            <h2>
              Don&apos;t forget your favorites
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Save your favorite images. Share them with your friends. As much as you need.
              </span>
            </div>
            <Image src="/next.svg" alt="" width={128} height={128} />
        </div>
      </div>
      <div className="flex flex-col justify-center p-48">
        <div className="flex flex-row justify-between max-w-full material-card">
            <Image src="/next.svg" alt="" width={128} height={128} />
            <div className="flex flex-col justify-center">
            <h2>
              Free for all
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Licensed under Apache 2.0 and published to F-Droid. All sources are open and available on Github.
              </span>
            </div>
        </div>
      </div>
    </main>
    <Wave/>
    <footer className="flex flex-row justify-between p-16">
      <SmupeLogo/>
              <div className="flex flex-row items-center">
                <a href="/" className="p-2">Home</a>
                <a href="/" className="p-2">Source</a>
                <a href="/" className="p-2">Download</a>
                <a href="/docs" className="p-2">Documentation</a>
                <a href="/" className="p-2">Privacy policy</a>
                <a href="/" className="p-2">Donate</a>
              </div>
    </footer></>)}
