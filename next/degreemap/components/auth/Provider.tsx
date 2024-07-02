"use client";

import { ReactNode } from "react";
import { SessionProvider } from "next-auth/react";

interface Props {
    children: ReactNode,
}

function Provider({ children }: Props) {
    return <SessionProvider>{children}</SessionProvider>;
}

export default Provider;