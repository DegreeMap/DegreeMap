import React, { useState } from "react"
import { CourseCard } from "./CourseCard";
import { BlockColumn } from "./BlockColumn";
import { Button } from "./button";

interface TermColumnProps {
    year: Year
    handleTermNameSave: (termId: number, newTermName: string) => void;
    handleEditCourse: (course: Course) => void;
    handleAddCourse: (yearId: number, termId: number) => void;
    handleAddBlock: (yearId: number, termId: number) => void;
}

export const TermColumn: React.FC<TermColumnProps> = ({ year, handleAddCourse, handleAddBlock, handleEditCourse, handleTermNameSave }) => {
    const [dropdownOpen, setDropdownOpen] = useState<{yearId: number; termId: number} | null>(null);
    const [editingTermId, setEditingTermId] = useState<number | null>(null);
	const [termNameDraft, setTermNameDraft] = useState<string>("");

    return (
        <div className="flex gap-x-2 h-full">
            {year.terms.map((term) => (
                <div key={term.id} className="w-24 max-w-sm flex flex-col group relative">
                    {editingTermId === term.id ? (
                        // Input Term Component
                        <input
                            type="text"
                            value={termNameDraft}
                            onChange={(e) => setTermNameDraft(e.target.value)}
                            onBlur={() => {
                                handleTermNameSave(term.id, termNameDraft)
                                setEditingTermId(null)
                                setTermNameDraft("")
                            }}
                            autoFocus
                            className="text-sm font-medium mb-2 bg-white border rounded px-2 py-1 w-full"
                        />
                    ) : (
                        <h3
                            className="text-sm font-medium mb-2 cursor-text"
                            onClick={() => {
                                setEditingTermId(term.id);
                                setTermNameDraft(term.name);
                            }}
                        >
                            {term.name}
                        </h3>
                    )}
                    <div className="bg-white border p-2 rounded flex-1 flex flex-col items-center overflow-hidden">
                        <div className="space-y-1 w-full overflow-auto">
                            {/* Course / Block Container*/}
                            {term.courses.map((course) => (
                                <div key={course.id} className={"p-1 w-full rounded text-sm"}>
                                    <CourseCard
                                        key={course.id}
                                        title={course.title}
                                        code={course.code}
                                        credits={course.credits}
                                        onClick={() => handleEditCourse(course)}
                                    />
                                </div>
                            ))}
                            {term.blocks.map((block) => (
                                <div key={block.id} className={"p-1 w-full rounded text-sm"}>
                                    <BlockColumn
                                        key={block.id}
                                        title={block.title}
                                    />
                                </div>
                            ))}
                        </div>
                        <div className="mt-2 absolute bottom-2 right-2 opacity-0 scale-95 
                        transition-all duration-200 ease-in-out group-hover:opacity-100 group-hover:scale-100 flex items-center justify-center">
                            <Button
                                color="bg-gray-500"
                                onClick={() =>
                                    setDropdownOpen(
                                        dropdownOpen?.yearId === year.id && dropdownOpen?.termId === term.id
                                            ? null
                                            : { yearId: year.id, termId: term.id }
                                    )
                                }
                            >
                                +
                            </Button>
                            {dropdownOpen?.yearId === year.id && dropdownOpen?.termId === term.id && (
                            <div className="absolute z-50 mt-2 w-32 bg-white border rounded shadow left-0">
                                <button
                                    className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                                    onClick={() => {
                                        handleAddCourse(year.id, term.id)
                                        setDropdownOpen(null)
                                    }}
                                >
                                    Add Course
                                </button>
                                <button
                                    className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                                    onClick={() => {
                                        handleAddBlock(year.id, term.id)
                                        setDropdownOpen(null)
                                    }}
                                >
                                    Add Block
                                </button>
                            </div>
                        )}
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}