import React, { useState } from "react"
import { CourseCard } from "./CourseCard";
import { BlockColumn } from "./BlockColumn";
import { Button } from "./button";

interface TermColumnProps {
    year: Year
    handleTermNameSave: (termId: number, newTermName: string) => void;
    handleEditCourse: (course: Course) => void;
    onRequestDropdownOpen: (yearId: number, termId: number, rect: DOMRect) => void;
    isDropdownOpenForTerm: (termId: number) => boolean
}

export const TermColumn: React.FC<TermColumnProps> = ({ year, handleEditCourse, handleTermNameSave, onRequestDropdownOpen , isDropdownOpenForTerm}) => {
    const [editingTermId, setEditingTermId] = useState<number | null>(null);
	const [termNameDraft, setTermNameDraft] = useState<string>("");

    return (
        <div className="flex gap-x-2 h-full">
            {year.terms.map((term) => (
                <div key={term.id} className="group w-[5.5rem] max-w-sm flex flex-col relative overflow-hidden">
                    {editingTermId === term.id ? (
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
                	<div className="bg-white border p-1 rounded flex-1 flex flex-col items-center overflow-y-auto max-h-[80vh] scrollbar-hidden">
                        <div className="space-y-1 w-full ">
                            {/* Course / Block Container*/}
                            {term.blocks.map((block) => (
                                <div key={block.id} className={"p-1 w-full rounded text-sm"}>
                                    <BlockColumn
                                        key={block.id}
                                        title={block.title}
                                    />
                                </div>
                            ))}
                            {term.courses.map((course) => (
                                <div key={course.id} className={"p-1 w-full rounded text-sm"}>
                                    <CourseCard
                                        id={course.id}
                                        key={course.id}
                                        title={course.title}
                                        code={course.code}
                                        credits={course.credits}
                                        color={course.color}
                                        onCourseChange={handleEditCourse}
                                    />
                                </div>
                            ))}
                        </div>
                        {}
                        <div 
	                        className={`mt-2 mb-24 flex items-center justify-center transition-all duration-100 ease-in-out ${
	                        	isDropdownOpenForTerm(term.id)
	                        		? "opacity-100 scale-100"
	                        		: "opacity-0 scale-95 group-hover:opacity-100 group-hover:scale-100"
	                        }`}
                        >
                            <Button
                                color="bg-gray-500"
                                onClick={(e) => {
                                    const rect = e.currentTarget.getBoundingClientRect();
                                    onRequestDropdownOpen(year.id, term.id, rect);
                                }}
                            >
                                +
                            </Button>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}