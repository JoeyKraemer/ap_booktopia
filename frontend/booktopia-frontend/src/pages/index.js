import Head from "next/head";
import {Geist, Geist_Mono} from "next/font/google";
import styles from "@/styles/Home.module.css";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export default function Home() {
    return (
        <>
            <Head>
                <title>Booktopia</title>
                <meta name="description" content="Booktopia"/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <link rel="icon" href=""/>
            </Head>
            <main className={`${styles.main} ${geistSans.variable} ${geistMono.variable}`}>
                <div className={styles.page}>
                    <h1 className={styles.title}>
                        Booktopia
                    </h1>
                </div>
            </main>
        </>
    );
}
