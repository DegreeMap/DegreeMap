import React from "react";

interface CourseCardProps {
	title?: string;
	code?: string;
	credits?: number;
    onClick?: () => void;
}

export const CourseCard: React.FC<CourseCardProps> = ({ title, code, credits }) => {
	return (
		<div className="bg-orange-400 text-white text-xs text-center rounded-t px-1 py-1">
			{title}
			<div className="bg-white text-black font-bold mt-1 rounded-b px-1">
				<h3 className="text-[.7rem] font-semibold">{code}</h3>
				<h3 className="text-[.7rem] font-semibold">({credits})</h3>
			</div>
		</div>
	);
};
