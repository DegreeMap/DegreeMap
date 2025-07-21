"use client"

import React, { useState } from "react";
import NavBar from "@/components/nav/navbars";
import { Button } from "@/components/ui/button";
import { CourseCard } from "@/components/ui/CourseCard";
import { CourseCardModal } from "@/components/ui/CourseCardModal";
import { BlockColumn } from "@/components/ui/BlockColumn";
import { TermColumn } from "@/components/ui/TermColumn";

export default function DegreeMapMaker() {
    const [years, setYears] = useState<Year[]>([]);
	const [nextId, setNextId] = useState(1);
    const [editingYearId, setEditingYearId] = useState<number | null>(null);
	const [yearNameDraft, setYearNameDraft] = useState<string>("");
    const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);

    const handleEditCourse = (course: Course) => {
    	setSelectedCourse(course);
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

    const handleAddCourse = (yearId: number, termId: number) => {
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
    }

    const handleAddBlock = (yearId: number, termId: number) => {
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
    };
    

	const handleYearNameSave = (yearId: number) => {
		setYears((prev) =>
			prev.map((year) =>
				year.id === yearId ? { ...year, name: yearNameDraft } : year
			)
		);
		setEditingYearId(null);
	};

    const handleTermNameSave = (termId: number, newTermName: string) => {
		setYears((prev) =>
			prev.map((year) => ({
				...year,
				terms: year.terms.map((term) =>
					term.id === termId ? { ...term, name: newTermName } : term
				),
			}))
		);
	};

	return (
		<div className="h-screen flex flex-col">
			<NavBar />
            {/* DegreeMap Container */}
            <div className="flex-1 w-full mx-auto overflow-hidden bg-gray-200">
                {/* Year Container */}
                <div className="flex gap-x-2 overflow-x-auto h-full py-4 pl-4">
                    {years.map((year) => (
						<div key={year.id} className="border p-3 rounded-lg bg-gray-100 flex-shrink-0 h-full flex flex-col">
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
                            <TermColumn
                                year={year}
                                handleAddBlock={handleAddBlock}
                                handleAddCourse={handleAddCourse}
                                handleEditCourse={handleEditCourse}
                                handleTermNameSave={handleTermNameSave}
                            />
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
