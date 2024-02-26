# Smupe!

> [!WARNING]
> **Smupe!** currently in early early alpha stage. Report any bugs to issues and contribute!

"Show me 'ur pictures, eternally!"

Dive into an endless stream of pics with **Smupe!**

Choose from a variety of image sources, from art & landscapes to memes & animals, and immerse yourself in a savor the everlasting viewing experience.

## Swipe, enjoy, repeat

**Smupe!** is easy to use. Just swipe to get a new image!

Found something interesting? Tap ~~twice~~ on heart to save it to Favourites!

Want to share or view original? ~~Swipe right~~ tap on action button to show context buttons!

Get bored with such stream of images? Switch to another one, the API-DEFs tab contain some built-in, and you can add your own.

## Features

* Easy-to-use interface based on Material 3 design
* Ability to change and define own image sources [WIP]
* Tag and query search support [WIP]
* List of favorite images
* Ability to share, open in browser and save
* Light and Dark themes
* Multiple languages [WIP]

## Thanks

* [stngularity](https://github.com/stngularity) - app icon and name, app testing and feedback
* [Jabka](https://github.com/Jabka-M) - helped me to figure out layout of app (but on Python)

## FAQ

<details>

<summary>

### Technical

</summary>

#### What versions of Android is supported?

Android 7 Nougat (API 24) and above should work.

Tested Android versions:

* Android 9 Pie (API 28)
* Android 13 Tiramisu (API 33)
* Android 14 Upside Down Cake (API 34)

#### Where I can download the app?

You can download release APK from [GitHub releases](https://github.com/EgorBron/Smupe/releases/).

There is no release in any app stores yet, but soon,
I will prepare for publishing in Play Market and F-Droid.

#### Will there be versions for Watch OS/Android TV/desktop platforms/iOS/Web?

Support for Watch OS & Android TV is planned, but not in the near future.

Web version is also will be done. Stay tuned for updates!

Desktop and iOS versions will require migration to the Compose Multiplatform (or the more radical way - Flutter).
To do so, I need to rewirite some parts of app logic entirely.
That's really time-consuming, so I'm not going to do it soon.

#### Can I use \<insert-any-image-source-here>?

Proably! As long as it matches all requirements of API-DEF specs. You'll need to write a new API-DEF (i.e. request schema processor) for it.

If you about the copyright... Use at your own risk. I have no warranty or liability for any claim, damage or any other kind, because app is just an interface to interact with such. It's not mine or app's job to check, the images are safe to retrieve or use, or not.

#### Will be there a (new/better) translation to \<language>?

There is two ways:

* You can request a language in [issues](https://github.com/EgorBron/Smupe/issues/new/), but it will have a worse quality since I may not know it and I will use a translator.
* You can [fork](https://github.com/EgorBron/Smupe/fork) the project, follow [translation guide](./Contributing.md#Translations) and then send me a pull request.
* What? Crowdin? I'm too lazy to set up it right now...

#### The app is slow/crashes/freezes/works not as expected/etc.

Please, [create an issue](https://github.com/EgorBron/Smupe/issues/new/) and describe your problem.
Currently, I was added an experimental exception log to my server using ACRA,
but it may be unstable, so please, provide full information how to reproduce your problem
(attach screen recording, version of Android, etc.).

#### How to build app by myself?

Check [Building](./Building.md) and/or [Contributing](./Contributing.md).

#### Can I contribute?

Of course! See [Contributing](./Contributing.md) to know how you can.

</details>

<details>

<summary>

### Other

</summary>

#### Why is it called `Smupe!`?

When I showed this app to my friends first time, I won't matter about the name. It had only work name - "trpp" or something, can't remember...

I asked, "How it should be named?", and *stngularity* said: "What about Picshow? Or SMYP, which is short for "Show Me Your Pictures"?".

I was like: "Oh, it's not that bad, I'll use it, but need to correct a bit".

So, finally, I decided:

> **Smupe!** called like that because it's stands for "Show me 'ur pictures, eternally!"

#### The code is bad 😬! No best practices used!

Calm down! That's my first expirience in Compose!

Instead of throwing rotten tomatoes at me, better help improve the app!

> For example, I want to get rid of side effects and buisness logic in composables, properly share data between them, use coroutines much more, etc.

See [Contributing](./Contributing.md) to know how you can help!

#### Missing comments/doc-comments

I know, I know. I'm just a lazy guy. In near future I'll add them.

#### Is any docs available?

Yes, there is manual docs and ~~Javadoc~~ Dokka available.

P. S. Dokka is not configured properly yet (because Next.js breaks with raw HTML in `public`).

</details>

## License

Licensed under the [Apache License 2.0](./LICENSE.txt).

```
Copyright 2024 EgorBron

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
