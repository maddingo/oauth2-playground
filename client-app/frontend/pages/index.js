import Head from 'next/head';
import Layout from "../components/layout";
import Quotes from "../components/quotes";


export default function Home() {
  return (
    <Layout>
      <Quotes/>
    </Layout>
  )
}
