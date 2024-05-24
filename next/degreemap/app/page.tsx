"use client"

import React, { useEffect, useState } from "react";
import axios from "axios";


export default function Home() {

  type Greeting = {
    id: number;
    content: string;
  }

  const [greetings, setGreetings] = useState<Greeting[]>([])

  const fetchData = () => {
    axios.get("http://localhost:8080/api/greetings")
    .then(response => {
      setGreetings(response.data)
    })
  }

  useEffect(() => {
    fetchData();
  }, [])
  
  return (
    <>
      <h1>GREETINGS</h1>
      {greetings.map(greeting => (
        <h3 key={greeting.id}>{greeting.id + " " + greeting.content}</h3>
      ))}
    </>
  );
}
