import style from './userinfo.module.css';
import Link from "next/link";
import Image from "next/image";
import {useSession} from "next-auth/react";

export default function UserInfo() {
  const {data: session} = useSession();
  return (
    <div className={style.panel}>
      {!session && (
        <>
        <Link href={'/oauth2/authorization/messaging-client-oidc'}>Sign In</Link>
        {/*<Link href={'/login'}>Sign In</Link>*/}
        </>
      )}
      {session && (
        <>
          Signed in as {session.user.email} <Image src={'/pingu_hahn.png'} alt={session.user.name}/> (<Link href="/logout" >Sign out</Link>)
        </>
      )}
    </div>
  )
}