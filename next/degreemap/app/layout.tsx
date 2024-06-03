import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";

// Authentication imports:
import SessionProvider from "@/components/SessionProvider";
import { getServerSession } from "next-auth";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "DegreeMap",
  description: "A web application that helps you visualize and plan your college course schedule through customizable flowcharts.",
  icons: ["./images/poop.jpg"],
};

export default async function RootLayout({children,}: Readonly<{children: React.ReactNode;}>) {
  const session = await getServerSession();
 
  return (
    <html lang="en">
      <body className={inter.className}>
        <SessionProvider session={session}>
          {children}
        </SessionProvider>
      </body>
    </html>
  );
}
