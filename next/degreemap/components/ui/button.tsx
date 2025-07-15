import React from "react";

interface CustomButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	color?: string; 
    // ^ background color (will be passed in as 'bg-blue-600' or something like that)
}

export function Button({ children, color = "bg-blue-600", className = "", ...props }: CustomButtonProps) {
	return (
		<button
			className={`px-4 py-2 text-white rounded ${color} ${className}`}
			{...props}
		>
			{children}
		</button>
	);
}

