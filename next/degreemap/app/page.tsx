"use client"

import React, { useEffect, useState } from "react";
import axios from "axios";
import NavBar from "@/components/nav/navbars";

export default function Home() {
  return (
    <div className = "h-screen w-screen">
      <NavBar></NavBar>
      <p>home page! :)</p>
    </div >
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
