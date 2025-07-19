"use client"

import React, { useState } from "react";
import NavBar from "@/components/nav/navbars";
import { Button } from "@/components/ui/button";
import { CourseCard } from "@/components/ui/CourseCard";
import { CourseCardModal } from "@/components/ui/CourseCardModal";

// interface CourseBlock {
// 	id: number;
// 	name: string; // TODO delete soon
// 	type: "course" | "block";
// 	title?: string;
// 	code?: string;
// 	credits?: number;
// }

interface Course {
	id: number;
	title: string;
	code: string;
	credits: number;
}

interface Block {
	id: number;
	title: string;
}

interface Term {
	id: number;
	name: string;
	courses: Course[];
	blocks: Block[];
}

interface Year {
	id: number;
	name: string;
	terms: Term[];
}

export default function DegreeMapMaker() {
    const [years, setYears] = useState<Year[]>([]);
	const [nextId, setNextId] = useState(1);
	const [dropdownOpen, setDropdownOpen] = useState<{yearId: number; termId: number} | null>(null);
    const [editingYearId, setEditingYearId] = useState<number | null>(null);
	const [yearNameDraft, setYearNameDraft] = useState<string>("");
    const [editingTermId, setEditingTermId] = useState<number | null>(null);
	const [termNameDraft, setTermNameDraft] = useState<string>("");
    const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);
    const [isModalOpen, setModalOpen] = useState(false);

    const handleEditCourse = (course: Course) => {
    	setSelectedCourse(course);
    	setModalOpen(true);
    };

    const handleSaveCourse = (updated: { title: string; code: string; credits: number }) => {
    	setYears((prev) =>
    		prev.map((year) => ({
    			...year,
    			terms: year.terms.map((term) => ({
    				...term,
    				blocks: term.blocks.map((b) =>
    					b.id === selectedCourse?.id ? { ...b, ...updated } : b
    				),
    			})),
    		}))
    	);
    	setModalOpen(false);
    	setSelectedCourse(null);
    };

	const addYear = () => {
		const newYear: Year = {
			id: nextId,
			name: `Year ${years.length + 1}`,
			terms: ["Fall", "Spring", "Summer"].map((term, index) => ({
				id: nextId + index + 1,
				name: term,
                courses: [],
				blocks: []
			})),
		};
		setNextId((id) => id + 4);
		setYears([...years, newYear]);
	};

    const addCourseToTerm = (yearId: number, termId: number) => {
        const newCourse: Course = {
            id: nextId,
            title: "Degree Map I",
            code: "DEGM-101",
            credits: 4
        }

        setYears((prevYears) =>
            prevYears.map((year) => year.id !== yearId? year: {
                ...year,
                terms: year.terms.map((term) =>
                    term.id !== termId ? term: {
                        ...term, 
                        courses: [...term.courses, newCourse],
                    }
                ),
            })
        );
        setNextId((id) => id + 1);
        setDropdownOpen(null);
    }

    const addBlockToTerm = (yearId: number, termId: number) => {
        const newBlock: Block = {
            id: nextId,
            title: `Internship`,
        };

        setYears((prevYears) =>
            prevYears.map((year) => year.id !== yearId? year: {
                ...year,
                terms: year.terms.map((term) =>
                    term.id !== termId ? term: {
                        ...term, 
                        blocks: [...term.blocks, newBlock],
                    }
                ),
            })
        );
        setNextId((id) => id + 1);
        setDropdownOpen(null);
    };
    

	const handleYearNameSave = (yearId: number) => {
		setYears((prev) =>
			prev.map((year) =>
				year.id === yearId ? { ...year, name: yearNameDraft } : year
			)
		);
		setEditingYearId(null);
	};

    const handleTermNameSave = (termId: number) => {
		setYears((prev) =>
			prev.map((year) => ({
				...year,
				terms: year.terms.map((term) =>
					term.id === termId ? { ...term, name: termNameDraft } : term
				),
			}))
		);
		setEditingTermId(null);
	};

	return (
		<div className="h-screen flex flex-col">
			<NavBar />
            {/* DegreeMap Container */}
            <div className="flex-1 w-full mx-auto overflow-hidden bg-gray-200">
                {/* Year Container */}
                <div className="flex gap-x-2 overflow-x-auto h-full py-4 pl-4">
                    {years.map((year) => (
						<div key={year.id} className="border p-3 rounded-lg bg-gray-100 flex-shrink-0">
							{editingYearId === year.id ? (
								// Input Year Component
                                <input
									type="text"
									value={yearNameDraft}
									onChange={(e) => setYearNameDraft(e.target.value)}
									onBlur={() => handleYearNameSave(year.id)}
									autoFocus
									className="text-base font-semibold mb-2 bg-white border rounded px-2 py-1 w-full"
								/>
							) : (
								<h2
									className="text-base font-semibold mb-2 cursor-text"
									onClick={() => {
										setEditingYearId(year.id);
										setYearNameDraft(year.name);
									}}
								>
									{year.name}
								</h2>
							)}
                            {/* Term Container */}
							<div className="flex gap-x-2 h-full">
								{year.terms.map((term) => (
                                    <div key={term.id} className="w-24 max-w-sm">
                                        {editingTermId === term.id ? (
                                            // Input Term Component
                                            <input
							            		type="text"
							            		value={termNameDraft}
							            		onChange={(e) => setTermNameDraft(e.target.value)}
							            		onBlur={() => handleTermNameSave(term.id)}
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
                                        <div className="bg-white border p-2 rounded relative flex flex-col items-center">
                                            <div className="space-y-1 w-full">
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
                                                        <div className="bg-blue-300 p-1 rounded text-sm">{block.title}</div>
                                                    </div>
                                                ))}
                                                <CourseCardModal
	                                                course={
	                                                	selectedCourse?.title && selectedCourse?.code && selectedCourse?.credits !== undefined
	                                                		? selectedCourse as { title: string; code: string; credits: number }
	                                                		: { title: "", code: "", credits: 0 }
	                                                }
                                                    isOpen={isModalOpen}
                                                    onClose={() => setModalOpen(false)}
                                                    onSave={handleSaveCourse}
                                                />
                                            </div>
                                            <div className="mt-2 relative items-center justify-center">
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
														onClick={() => addCourseToTerm(year.id, term.id)}
													>
														Add Course
													</button>
													<button
														className="block w-full text-left px-4 py-2 hover:bg-gray-100"
														onClick={() => addBlockToTerm(year.id, term.id)}
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
						</div>
					))}
					<div className="flex items-center">
						<Button onClick={addYear}>+</Button>
					</div>
				</div>
			</div>
		</div>
	);
}
