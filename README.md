Script Runner Lite
=======

Flatdoc is a small JavaScript file that fetches Markdown files and renders them
as full pages. Essentially, it's the easiest
way to make open source documentation from *Readme* files.

 * No server-side components
 * No build process needed
 * Deployable via GitHub Pages
 * Can fetch GitHub Readme files
 * Gorgeous default theme (and it's responsive)
 * Just create an HTML file and deploy!

Getting started
---------------

Create a file based on the template, which has a bare DOM, link to the
scripts, and a link to a theme. It will look something like this (not exact).
For GitHub projects, simply place this file in your [GitHub pages] branch and
you're all good to go.

*In short: just download this file and upload it somewhere.*

The main JS and CSS files are also available in [npm] and [bower].

[Default theme template >][template]

[Blank template >][blank]

[bower]: http://bower.io/search/?q=flatdoc
[npm]: https://www.npmjs.org/package/flatdoc

How it works
------------

Flatdoc is a hosted `.js` file (along with a theme and its assets) that you can
add into any page hosted anywhere.

#### All client-side

There are no build scripts or 3rd-party services involved. Everything is done in
the browser. Worried about performance? Oh, It's pretty fast.

Flatdoc utilizes the [GitHub API] to fetch your project's Readme files. You may
also configure it to fetch any arbitrary URL via AJAX.

#### Lightning-fast parsing

Next, it uses [marked], an extremely fast Markdown parser that has support for
GitHub flavored Markdown.

Flatdoc then simply renders *menu* and *content* DOM elements to your HTML
document. Flatdoc also comes with a default theme to style your page for you, or
you may opt to create your own styles.