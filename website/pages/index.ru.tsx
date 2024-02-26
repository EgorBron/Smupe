import Image from "next/image"
import pic1 from "../public/img1.png"
import pic2 from "../public/img2.png"
import pic4 from "../public/img4.png"
import pic5 from "../public/img5.png"
import Link from "next/link"

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
<header className="flex flex-row justify-between p-8 pt-4 max-w-max min-w-full nav-wrap material-primary">
      <SmupeLogo/>
      <div className="flex flex-row items-center nav-wrap">
        <a href="/" className="p-2">Главная</a>
        <a href="https://github.com/EgorBron/Smupe" className="p-2">Исходный код</a>
        <a href={"https://cdn.discordapp.com/attachments/784756501612986409/1211584551264981042/app-foss-debug.apk?ex=65eebb0b&is=65dc460b&hm=bebe7618933c6bceb5da3b7270fdb50ad34e020f51f54b0e8e5a44c98cf9c1e7&"} className="p-2">Скачать</a>
        <a href="/docs" className="p-2">Документация</a>
        <a href="/" className="p-2">Политика конфидециальности</a>
      </div>
    </header>
    <main className="">
      <div className="flex flex-col lg:p-48 sm:p-8 pt-8 pb-8">
        <div className="flex flex-row justify-between max-w-full material-card">
          <div className="flex flex-col">
            <h1 className="main-header">
              И смотрите картинки, вечно
            </h1>
            <span style={{
              fontSize: '1.5rem',
              marginBottom: '2rem',
              maxWidth: '25rem',
            }}>
              Smupe! позволяет смотреть бесконечный поток картинок, которые Вам нравятся.
            </span>
            <div className="flex flex-row justify-stretch">
              <a href={"https://cdn.discordapp.com/attachments/784756501612986409/1211584551264981042/app-foss-debug.apk?ex=65eebb0b&is=65dc460b&hm=bebe7618933c6bceb5da3b7270fdb50ad34e020f51f54b0e8e5a44c98cf9c1e7&"}><button className="material-button">Скачать</button></a>
            </div>
          </div>
          <div>
            <Image style={{borderRadius: '1rem', overflow: 'scroll'}} src="/smupe_icon.svg" alt="logo" width={128} height={128} />
          </div>
        </div>
      </div>
      <Wave/>
      <div className="flex flex-col justify-center lg:p-48 sm:p-8 pt-0 pb-0">
        <div className="flex flex-row justify-between max-w-full material-card">
          <div className="flex flex-col justify-center">
            <h2>
              Просто смахивайте и наслаждайтесь
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Со Smupe! Вы сможете лекго просматривать изображения почти откуда угодно.
              </span>
            </div>
            <Image src={pic1} alt="" style={{maxWidth: '20%', maxHeight: '20%',borderRadius: '1rem', margin: '2rem', marginRight: '5rem'}}/>
        </div>
      </div>
      <div className="flex flex-col justify-center lg:p-48 sm:p-8 pt-48 pb-48">
        <div className="flex flex-row justify-between max-w-full material-card">
        <Image src={pic2} alt="" style={{maxWidth: '20%', maxHeight: '20%',borderRadius: '1rem', margin: '2rem', marginLeft: '5rem'}}/>
            <div className="flex flex-col justify-center">
            <h2>
              Гибко, насколько это возможно
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Смотрите только что Вам нравится, благодаря лёгкому в настройке списку источников изображений.
              </span>
            </div>
        </div>
      </div>
      <div className="flex flex-col justify-center lg:p-48 sm:p-8 pt-0 pb-0">
        <div className="flex flex-row justify-between max-w-full material-card">
          <div className="flex flex-col justify-center">
            <h2>
              Не забывайте про самых-самых
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Сохраняйте любимые изображения. Делитесь с друзьями. Столько, сколько пожелаете.
              </span>
            </div>
            <Image src={pic4} alt="" style={{maxWidth: '20%', maxHeight: '20%',borderRadius: '1rem', margin: '2rem', marginRight: '5rem'}}/>
        </div>
      </div>
      <div className="flex flex-col justify-center lg:p-48 sm:p-8 pt-48">
        <div className="flex flex-row justify-between max-w-full material-card">
        <Image src={pic5} alt="" style={{maxWidth: '20%', maxHeight: '20%',borderRadius: '1rem', margin: '2rem', marginLeft: '5rem'}}/>
            <div className="flex flex-col justify-center">
            <h2>
              Свободно для всех
            </h2>
            <span style={{
                fontSize: '1.2rem',
                marginBottom: '2rem',
                maxWidth: '25rem',
              }}>
                Выпущено под лицензией Apache 2.0 и опубликовано в F-Droid. Все исходники открыты и доступны на Github.
              </span>
            </div>
        </div>
      </div>
    </main>
    <Wave/>
    <footer className="flex flex-row justify-between p-16">
      <SmupeLogo/>
              <div className="flex flex-row items-center flex-wrap">
              <a href="/" className="p-2">Главная</a>
              <a href="https://github.com/EgorBron/Smupe" className="p-2">Исходный код</a>
              <a href={"https://cdn.discordapp.com/attachments/784756501612986409/1211584551264981042/app-foss-debug.apk?ex=65eebb0b&is=65dc460b&hm=bebe7618933c6bceb5da3b7270fdb50ad34e020f51f54b0e8e5a44c98cf9c1e7&"} className="p-2">Скачать</a>
              <a href="/docs" className="p-2">Документация</a>
              <a href="/" className="p-2">Политика конфидециальности</a>
              </div>
    </footer></>)}
