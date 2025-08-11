import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Provider from "@/components/auth/Provider";
import 'bootstrap/dist/css/bootstrap.min.css';

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "DegreeMap",
  description: "A web application that helps you visualize and plan your college course schedule through customizable flowcharts.",
};

export default async function RootLayout({children,}: Readonly<{children: React.ReactNode;}>) { 
  return (
    <html lang="en">
      <body className={inter.className}>
        <Provider>
          {children}  
        </Provider>
      </body>
    </html>
  );
}
