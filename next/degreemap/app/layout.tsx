import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { AuthProvider } from "@/context/AuthContext";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "DegreeMap",
  description: "A web application that helps you visualize and plan your college course schedule through customizable flowcharts.",
};

export default async function RootLayout({children,}: Readonly<{children: React.ReactNode;}>) { 
  return (
    <html lang="en">
      <body className={inter.className}>
        <AuthProvider>
          {children}  
        </AuthProvider>
      </body>
    </html>
  );
}
