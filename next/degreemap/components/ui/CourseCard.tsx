import React, { useState } from "react";
import { HexColorPicker } from "react-colorful";

interface CourseCardProps {
	id: number,
	title: string;
	code: string;
	credits: number;
	color: string;
	selected: boolean;
	onClick: () => void;

	onCourseChange: (course: Course) => void;
	onColorChange?: (newColor: string) => void;
}


export const CourseCard: React.FC<CourseCardProps> = ({ id, title, code, credits, color, selected, onCourseChange, onClick }) => {
	const [colorPickerOpen, setColorPickerOpen] = useState<Boolean>(false)
	
	const handleBlur = (
		e: React.FocusEvent<HTMLDivElement>,
		callback?: (val: any) => void,
		convert?: (val: string) => any
	) => {
		if (callback) {
			const val = e.currentTarget.innerText.trim();
			callback(convert ? convert(val) : val);
		}
	};

	const onTitleChange = (newTitle: string) => {
		onCourseChange({
			id: id,
			title: newTitle, 
			code: code, 
			credits: credits,
			color: color
		})
	}

	const onCodeChange = (newCode: string) => {
		onCourseChange({
			id: id,
			title: title, 
			code: newCode, 
			credits: credits,
			color: color
		})
	}

	const onCreditsChange = (newCredits: number) => {
		onCourseChange({
			id: id,
			title: title, 
			code: code, 
			credits: newCredits,
			color: color
		})
	}

	const onColorChange = (newColor: string) => {
		onCourseChange({
			id, title, code, credits,
			color: newColor
		})
	}

	const colors = ['#f87171', '#facc15', '#4ade80', '#38bdf8', '#a78bfa'];

	return (
		<div 
			className={`
				text-white text-xs text-center rounded px-1 py-1 cursor-pointer
				transform transition duration-200 ease-in-out hover:scale-105 relative
				${selected ? "ring-2 ring-black" : ""}
			`}
			style={{backgroundColor: color }}
			onMouseEnter={() => setColorPickerOpen(true)}
			onMouseLeave={() => setColorPickerOpen(false)}
			onClick={onClick}
		>
			<div className="py-1 cursor-text outline-none focus:outline-none"
				contentEditable
				suppressContentEditableWarning
				onBlur={(e) => handleBlur(e, onTitleChange)}
				onInput={(e) => {
					const el = e.currentTarget;
					if (el.innerText.length > 20) {
						el.innerText = el.innerText.slice(0, 20);
						const range = document.createRange();
						const sel = window.getSelection();
						range.selectNodeContents(el);
						range.collapse(false);
						sel?.removeAllRanges();
						sel?.addRange(range);
					}
				}}
			>
				{title}
			</div>
			<div className="bg-white text-black font-bold mt-1 rounded-b px-1">
				<h3 className="text-[.6rem] font-semibold leading-tight pt-[3px] cursor-text outline-none focus:outline-none"
					contentEditable
					suppressContentEditableWarning
					onBlur={(e) => handleBlur(e, onCodeChange)}
					onInput={(e) => {
						const el = e.currentTarget;
						if (el.innerText.length > 10) {
							el.innerText = el.innerText.slice(0, 10);
							const range = document.createRange();
							const sel = window.getSelection();
							range.selectNodeContents(el);
							range.collapse(false);
							sel?.removeAllRanges();
							sel?.addRange(range);
						}
					}}
				>
					{code}
				</h3>
				<h3 className="text-[.6rem] font-semibold leading-none pb-[3px] cursor-text outline-none focus:outline-none"
					contentEditable
					suppressContentEditableWarning
					onBlur={(e) => handleBlur(e, onCreditsChange, (v) => parseInt(v))}
				>
					{`(${credits})`}
				</h3>
			</div>
			{/* {colorPickerOpen && (
				<div className="absolute top-1 right-[-160px] z-20 bg-white p-2 rounded shadow">
					<HexColorPicker color={color} onChange={onColorChange} />
				</div>
			)} */}
		</div>
	);
};
