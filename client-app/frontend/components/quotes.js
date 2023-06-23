import useSWR from 'swr';

// Get quotes from remote API
// TODO replace with call to the client-app
// Read: https://medium.com/technest/next-js-oauth-with-nextauth-js-53a9f2b9994f
// Read: https://next-auth.js.org/getting-started/client
// Read: https://www.devgould.com/jwt-authentication-with-nextjs-bff-backend-for-frontend/

export default function Quotes() {
  const fetcher = (url) => fetch(url).then((res) => res.json());
  const { data, fetchError } = useSWR('https://v2.jokeapi.dev/joke/Any?safe-mode', fetcher);
  if (fetchError) return <div>failed to load</div>;
  if (!data) return <div>loading...</div>;
  const { error, category, type, joke, flags, id, safe, lang} = data;
  return (
    <div>
      <h2>{data.category} Joke</h2>
      <>
        {
          data.type === 'single' ? (
            <p>{data.joke}</p>
          ) : (
            <>
              <p>{data.setup}</p>
              <p>{data.delivery}</p>
            </>
          )
        }
      </>
    </div>
  );
}
