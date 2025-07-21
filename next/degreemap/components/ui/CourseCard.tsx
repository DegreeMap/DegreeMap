import React from "react";

interface CourseCardProps {
	title?: string;
	code?: string;
	credits?: number;
    onClick?: () => void;
}

export const CourseCard: React.FC<CourseCardProps> = ({ title, code, credits, onClick }) => {
	return (
		<div 
			className="bg-orange-400 text-white text-xs text-center rounded px-1 py-1 cursor-pointer
					   transform transition duration-200 ease-in-out hover:scale-105"
			onClick={onClick}
		>
			<div className="py-1">{title}</div>
			<div className="bg-white text-black font-bold mt-1 rounded-b px-1">
				<h3 className="text-[.6rem] font-semibold leading-tight pt-[3px]">{code}</h3>
				<h3 className="text-[.6rem] font-semibold leading-none pb-[3px]">{`(${credits})`}</h3>
			</div>
		</div>
	);
};
