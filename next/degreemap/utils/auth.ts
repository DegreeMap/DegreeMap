// import axios from "axios";
// import { useSession } from "next-auth/react";

// export const useAuthRequewst = () => {
//     const { acccessToken, setAccessToken } = useSession();

//     const refreshToken = async () => {
//         const res = await axios.post("http://localhost:8080/api/auth/refresh", {}, { withCredentials: true });
//         if (res.status === 200) {
//             setAccessToken(res.data.accessToken);
//         }
//     }
// }