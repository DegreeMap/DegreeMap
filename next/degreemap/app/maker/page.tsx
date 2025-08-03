"use client"

import React, { useState } from "react";
import NavBar from "@/components/nav/navbars";
import { Button } from "@/components/ui/button";
import { TermColumn } from "@/components/ui/TermColumn";
import { Dropdown } from "@/components/ui/common/Dropdown";
import { Toolbar } from "@/components/ui/Toolbar";

export default function DegreeMapMaker() {
    const [years, setYears] = useState<Year[]>([]);
	const [nextId, setNextId] = useState(1);
    const [editingYearId, setEditingYearId] = useState<number | null>(null);
	const [yearNameDraft, setYearNameDraft] = useState<string>("");
	const [addDropdownOpen, setAddDropdownOpen] = useState<{ yearId: number; termId: number } | null>(null);
	const [dropdownPosition, setDropdownPosition] = useState<{ top: number; left: number } | null>(null);
	const [selectedCourses, setSelectedCourses] = useState<Course[]>([]);

	/**
	 * Adds a new course to the selectedCourses field
	 * @param newSelectedCourse: The course being selected
	 */
	const handleSelectCourse = (newSelectedCourse: Course) => {
		setSelectedCourses((prev) => {
			const exists  = prev.some(course => course.id == newSelectedCourse.id)
			// ^ exists is true if there is a course that shares the same id
			return exists
				? prev.filter(course => course.id !== newSelectedCourse.id)
				: [...prev, newSelectedCourse];
		})
	}

	const handleClearSelection = () => {
		setSelectedCourses([])
	}

	const handleDeleteSelection = () => {
		const selectedIds = new Set(selectedCourses.map(c => c.id));

		setYears((prev) => 
			prev.map((year) => ({
				...year,
				terms: year.terms.map((term) => ({
					...term,
					courses: term.courses.filter((course) => !selectedIds.has(course.id))
				}))
			})))
		setSelectedCourses([])
	}

    const handleEditCourse = (updated: Course) => {
    	setYears((prev) =>
    		prev.map((year) => ({
    			...year,
    			terms: year.terms.map((term) => ({
    				...term,
    				courses: term.courses.map((course) =>
    					course.id === updated.id ? { ...course, ...updated } : course
    				),
    			})),
    		}))
    	);
    };

	const handleBulkEditColorCourse = (hex: string) => {
		const selectedIds = new Set(selectedCourses.map(c => c.id));

		setYears((prev) =>
		  prev.map(year => ({
			...year,
			terms: year.terms.map((term) => ({
			  ...term,
			  courses: term.courses.map((course) =>
				selectedIds.has(course.id)
				  ? { ...course, color: hex } 
				  : course
			  ),
			})),
		  }))
		);
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
            credits: 4,
			color: '#f97316'
        }

        setYears((prevYears) =>
            prevYears.map((year) => year.id !== yearId? year: {
                ...year,
                terms: year.terms.map((term) =>
                    term.id !== termId ? term: {
                        ...term, 
                        courses: [...term.courses, newCourse],
						blocks: term.blocks
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
						courses: term.courses
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
			{/* <NavBar /> */}
			<Toolbar 
				selectedCourses={selectedCourses} 
				onBulkEditColor={handleBulkEditColorCourse}
				onClearSelection={handleClearSelection}
				onBulkDelete={handleDeleteSelection}
			/>
            {/* DegreeMap Container */}
            <div className="flex-1 w-full mx-auto bg-gray-200">
                {/* Year Container */}
                <div className="flex gap-x-2 h-full pb-4 pl-4">
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
								selectedCourses={selectedCourses}
								handleSelectCourse={handleSelectCourse}
                                handleEditCourse={handleEditCourse}
                                handleTermNameSave={handleTermNameSave}
								onRequestDropdownOpen={(yearId, termId, rect) => {
									setAddDropdownOpen({yearId, termId});
									setDropdownPosition({
										top: rect.bottom + window.scrollY,
										left: rect.left + window.scrollX,
									});
								}}
								isDropdownOpenForTerm={(termId) => addDropdownOpen?.termId === termId}
                            />
						</div>
					))}
					<div className="flex items-center">
						<Button onClick={addYear}>+</Button>
					</div>
				</div>
			</div>
			{addDropdownOpen && dropdownPosition && (
			<Dropdown
				isOpen={addDropdownOpen != null}
				onClose={() => setAddDropdownOpen(null)}
				position={dropdownPosition}
				options={[{
					option: "Add Course",
					action: () => handleAddCourse(addDropdownOpen.yearId, addDropdownOpen.termId)
				},{
					option: "Add Block",
					action: () => handleAddBlock(addDropdownOpen.yearId, addDropdownOpen.termId)
				}]}
		/>
		)}
		</div>
	);
}
