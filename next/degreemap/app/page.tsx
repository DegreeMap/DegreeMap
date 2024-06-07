"use client"

import React, { useEffect, useState } from "react";
import axios from "axios";
import NavBar from "@/components/nav/navbars";
import NavItem from "@/components/nav/navitem";
import { useSession } from "next-auth/react";

type NavData = {
  name: string;
  link: string;
};

export default function Home() {
  const { data: session } = useSession();
    
  var navItems: NavData[];
  if(session) {
    navItems = [
      { name: "Profile", link: "/profile" },
    ]
  }
  else {    
    navItems = [
      { name: "Sign In", link: "/profile" },
    ]
  }
  
  return (
    <div className = "h-screen w-screen">
      <NavBar navItems={navItems}></NavBar>
      <div> 
        <p>home page! :)</p>  
        <NavItem navData={{name: "Maker", link: "/maker"}}></NavItem>
        <NavItem navData={{name: "Courses", link: "/courses"}}></NavItem>  
      </div>
    </div>
  );
}

// export default function Home() {

//   type Greeting = {
//     id: number;
//     content: string;
//   }

//   const [greetings, setGreetings] = useState<Greeting[]>([])

//   const fetchData = () => {
//     axios.get("http://localhost:8080/api/greetings")
//     .then(response => {
//       setGreetings(response.data)
//     })
//   }

//   useEffect(() => {
//     fetchData();
//   }, [])
  
//   return (
//     <>
//       <h1>GREETINGS</h1>
//       {greetings.map(greeting => (
//         <h3 key={greeting.id}>{greeting.id + " " + greeting.content}</h3>
//       ))}
//     </>
//   );
// }
