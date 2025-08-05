import { title } from "process";
import React from "react";

interface BlockColumnProps {
	id: number
	title: string;
	selected: boolean;
	onBlockChange: (newBlock: Block) => void;
    onClick: () => void;
}


export const BlockColumn: React.FC<BlockColumnProps> = ({id, title, selected, onBlockChange, onClick }) => {
	const onTitleChange = (newTitle: string) => {
		onBlockChange({
			id: id,
			title: newTitle
		})
	}

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

	return (
		<div
			className={`
				w-full h-96 bg-blue-200 border rounded text-sm flex items-center justify-start text-left px-1 py-2 cursor-pointer
				transform transition duration-200 ease-in-out hover:scale-105 relative
				${selected ? "ring-2 ring-offset-2 ring-black" : ""}
				`}
			style={{
				writingMode: "vertical-rl",
				transform: "rotate(180deg)",
			}}
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
		</div>
	);
};