import Head from 'next/head';

export const siteTitle = 'OAuth2 Playground';
export const siteDescription = 'An set of applications demonstrating a typical application with OAuth2';

export default function Layout({ children}) {
  return (
    <div className={"container"}>
      <Head>
        <title>{siteTitle}</title>
        <link rel="icon" href="/favicon.ico" />
        <meta name="description" content={siteDescription} />
        <meta name="og:title" content={siteTitle}/>
      </Head>
      <header>
      </header>
      <main>{children}</main>
    </div>
  );
}
