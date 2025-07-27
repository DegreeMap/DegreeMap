import React from "react";

interface BlockColumnProps {
	title?: string;
    //onClick?: () => void;
}

export const BlockColumn: React.FC<BlockColumnProps> = ({ title }) => {
	return (
		<div
			className="w-full h-96 bg-blue-200 border text-sm text-center px-1 py-2 cursor-pointer"
			style={{
				writingMode: "vertical-rl",
				transform: "rotate(180deg)",
			}}
			// onClick={onClick}
		>
			{title}
		</div>
	);
};