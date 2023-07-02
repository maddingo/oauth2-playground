import Layout from "../components/layout";
import Quotes from "../components/quotes";
import {useSession} from "next-auth/react";

export default function Home() {
  const {data: session} = useSession()

  return (

      <Layout>

        <Quotes jokeUri = {'/api/joke'} withRefreshButton = {false} />
        {session ? (
            // This URL needs login
            <Quotes jokeUri = {'/api/joke1'} withRefreshButton = {true} />
          ) : (
            <></>
          )
        }
      </Layout>
  )
}
