import React, { useEffect, useState } from "react"
import { HexColorPicker } from "react-colorful";
import { FaSearchPlus, FaSearchMinus, FaCog, FaTrash, FaPencilAlt } from "react-icons/fa";
import { Button } from "./button";
import { CourseCardModal } from "./CourseCardModal";

interface ToolbarProps {
    selectedCourses: Course[],
    onBulkEditColor: (hex: string) => void,
    // onBulkEdit: () => void,
    onBulkDelete: () => void,
    onClearSelection: () => void,
}

export const Toolbar: React.FC<ToolbarProps> = ({ selectedCourses, onBulkEditColor, onClearSelection, onBulkDelete }) => {
    const [isColorPickerOpen, setIsColorPickerOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [tempColor, setTempColor] = useState<string>(selectedCourses[0]?.color ?? "#f97316");
    // ^ tempColor is the color displayed on the color picker button

    useEffect(() => {
        if (selectedCourses.length > 0) {
            setTempColor(selectedCourses[0].color ?? "#f97316");
        }
    }, [selectedCourses]
    );

    useEffect(() => {
        const onKey = (e: KeyboardEvent) => {
            if (e.key === "Escape") setIsColorPickerOpen(false);
        };
        if (isColorPickerOpen) window.addEventListener("keydown", onKey);
        return () => window.removeEventListener("keydown", onKey);
    }, [isColorPickerOpen]);

    const iconSize = 20;
    const selectionIconSize = 16;
    const iconStyle = "p-2 text-gray-800 hover:text-gray-600";

    return (
        <div className="bg-gray-200 w-full flex p-2 px-6 items-center text-white">
            <h1 className="text-lg font-semibold text-gray-800 pr-5">DegreeMap</h1>

            {selectedCourses.length > 0 ? (
                <div className="flex items-center gap-1">
                    <h2 className="text-base text-gray-800 mr-3">Selected: {selectedCourses.length}</h2>
                    <button
                        className="flex items-center gap-2 rounded border px-2 py-1 text-sm bg-white hover:bg-gray-100"
                        onClick={() => setIsColorPickerOpen(true)}
                    >
                        <span
                            className="inline-block h-4 w-4 rounded border"
                            style={{ backgroundColor: tempColor }}
                        />
                    </button>

                    <button className={iconStyle} onClick={() => setIsEditModalOpen(true)}>
                        <FaPencilAlt size={selectionIconSize} />
                    </button>

                    <button className={iconStyle} onClick={onBulkDelete}>
                        <FaTrash size={selectionIconSize} />
                    </button>
                    
                    <Button
                        className="bg-white text-gray-800 py-0 hover:bg-gray-100"
                        onClick={()=> onClearSelection()}
                    >Clear</Button>
                </div>
            ) : (
                <div></div>
            )}

            {/* Separates left and right side */}
            <div className="mr-auto" /> 

            <button className={iconStyle}>
                <FaSearchPlus size={iconSize} />
            </button>

            <button className={iconStyle}>
                <FaSearchMinus size={iconSize} />
            </button>

            <button className={iconStyle}>
                <FaCog size={iconSize} />
            </button>

            {isEditModalOpen && (
                <>
                    <CourseCardModal 
                        isOpen={isEditModalOpen} 
                        onClose={() => setIsEditModalOpen(false)}
                    />

                    {/* <div
                        className="fixed inset-0 z-[1000] grid place-items-center bg-black/20"
                        onClick={() => setIsEditModalOpen(false)}
                        role="dialog"
                        aria-modal="true"
                    ></div> */}
                </>
            )}

            {isColorPickerOpen && (
                <>
                    <div
                        className="fixed inset-0 z-[1000] grid place-items-center bg-black/20"
                        onClick={() => setIsColorPickerOpen(false)}
                        role="dialog"
                        aria-modal="true"
                    >
                        <div
                            className="rounded-lg border bg-white p-3 shadow-xl"
                            onClick={(e) => e.stopPropagation()}
                        >
                            <HexColorPicker
                                color={tempColor}
                                onChange={(hex) => {
                                    setTempColor(hex);
                                    onBulkEditColor(hex);
                                }}
                            />
                        </div>
                    </div>
                </>
            )}
        </div>
    )
}