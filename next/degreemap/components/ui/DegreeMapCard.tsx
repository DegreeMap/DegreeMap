// components/DegreeMapCard.tsx
"use client";

import Link from "next/link";
import { FaTrash } from "react-icons/fa";

interface DegreeMapCardProps {
	degreeMap: DegreeMap;
	onRename?: (id: string, newName: string) => void;
	onDelete?: (id: string) => void;
}

export const DegreeMapCard: React.FC<DegreeMapCardProps> = ({ degreeMap, onRename, onDelete }) => {
    const selectionIconSize = 16;
    const iconStyle = "p-2 text-gray-800 hover:text-gray-600";

	return (
		<div className="
            rounded-2xl w-80 shrink-0 border bg-white p-4 shadow-sm
            transform transition duration-200 ease-in-out hover:scale-105 relative">
			<div className="flex items-start justify-between gap-3">
				<Link href={`/maker/${degreeMap.id}}`} className="text-lg font-semibold hover:underline">
					{degreeMap.name || "NULL_NAME"}
				</Link>

				<div className="flex gap-2 shrink-0">
                    <button className={iconStyle} onClick={() => onDelete?.(degreeMap.id)}>
                        <FaTrash size={selectionIconSize} />
                    </button>
				</div>
			</div>

			<p className="mt-2 text-xs text-gray-500">
				last updated {new Date(degreeMap.updatedAt).toLocaleString()}
			</p>
		</div>
	);
};
