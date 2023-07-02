import useSWR from 'swr';
import style from './quotes.module.css';

// Get quotes from remote API
// TODO replace with call to the client-app
// Read: https://medium.com/technest/next-js-oauth-with-nextauth-js-53a9f2b9994f
// Read: https://next-auth.js.org/getting-started/client
// Read: https://www.devgould.com/jwt-authentication-with-nextjs-bff-backend-for-frontend/

export default function Quotes({withRefreshButton = false, jokeUri = '/api/joke'}) {

  // const jokeUrl = 'https://v2.jokeapi.dev/joke/Any?safe-mode';
  const fetcher = (url) => fetch(url).then((res) => res.json());
  const { data, mutate, error } = useSWR(jokeUri, fetcher);
  if (error) return <div className={style.content}><div className={style.error}>failed to load</div></div>;
  if (!data) return <div className={style.content}><div className={style.loading}>loading...</div></div>;
  return (
    <div className={style.content}>
      <h2 className={style.header}>{data.category} Joke</h2>
      <div className={style.area}>
        {
          data.type === 'single' ? (
            <p className={style.single}>{data.joke}</p>
          ) : (
            <>
              <p className={style.setup}>{data.setup}</p>
              <p className={style.delivery}>{data.delivery}</p>
            </>
          )
        }
      </div>
        {
          withRefreshButton && (
            <div className={style.buttonPanel}>
            <button className={style.button} onClick={() => mutate(data)}>Refresh</button>
            </div>
          )
        }
    </div>
  );
}
