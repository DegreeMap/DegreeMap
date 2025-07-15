"use client"

import React, { useState } from "react";
import NavBar from "@/components/nav/navbars";
import { Button } from "@/components/ui/button";

interface CourseBlock {
    id: number;
	name: string;
	type: "course" | "block";
}

interface Term {
	id: number;
	name: string;
	blocks: CourseBlock[];
}

interface Year {
	id: number;
	name: string;
	terms: Term[];
}

export default function DegreeMapMaker() {
    const [years, setYears] = useState<Year[]>([]);
	const [nextId, setNextId] = useState(1);
	const [dropdownOpen, setDropdownOpen] = useState<{ yearId: number; termId: number } | null>(null);
    const [editingYearId, setEditingYearId] = useState<number | null>(null);
	const [yearNameDraft, setYearNameDraft] = useState<string>("");
    const [editingTermId, setEditingTermId] = useState<number | null>(null);
	const [termNameDraft, setTermNameDraft] = useState<string>("");

	const addYear = () => {
		const newYear: Year = {
			id: nextId,
			name: `Year ${years.length + 1}`,
			terms: ["Fall", "Spring", "Summer"].map((term, index) => ({
				id: nextId + index + 1,
				name: term,
				blocks: [],
			})),
		};
		setNextId((id) => id + 4);
		setYears([...years, newYear]);
	};

	const addBlockToTerm = (yearId: number, termId: number, type: "course" | "block") => {
		setYears((prevYears) =>
			prevYears.map((year) => {
				if (year.id !== yearId) return year;
				return {
					...year,
					terms: year.terms.map((term) => {
						if (term.id !== termId) return term;
						return {
							...term,
							blocks: [
								...term.blocks,
								{ id: nextId, name: `${type} ${term.blocks.length + 1}`, type },
							],
						};
					}),
				};
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
            <div className="flex-1 overflow-auto p-4 max-w-[1400px] w-full mx-auto bg-gray-200">
                {/* Year Container */}
                <div className="flex gap-4 overflow-x-auto">
                    {years.map((year) => (
						<div key={year.id} className="border p-4 rounded-lg bg-gray-100 min-w-[220px]">
							{editingYearId === year.id ? (
								// Input Year Component
                                <input
									type="text"
									value={yearNameDraft}
									onChange={(e) => setYearNameDraft(e.target.value)}
									onBlur={() => handleYearNameSave(year.id)}
									autoFocus
									className="text-lg font-semibold mb-2 bg-white border rounded px-2 py-1 w-full"
								/>
							) : (
								<h2
									className="text-lg font-semibold mb-2 cursor-text"
									onClick={() => {
										setEditingYearId(year.id);
										setYearNameDraft(year.name);
									}}
								>
									{year.name}
								</h2>
							)}
                            {/* Term Container */}
							<div className="flex gap-4">
								{year.terms.map((term) => (
                                    <div key={term.id}>
                                        {editingTermId === term.id ? (
                                            // Input Term Component
                                            <input
							            		type="text"
							            		value={termNameDraft}
							            		onChange={(e) => setTermNameDraft(e.target.value)}
							            		onBlur={() => handleTermNameSave(term.id)}
							            		autoFocus
							            		className="text-lg font-semibold mb-2 bg-white border rounded px-2 py-1 w-40 max-w-sm"
							            	/>
							            ) : (
								            <h3
								            	className="text-lg font-semibold mb-2 cursor-text"
								            	onClick={() => {
								            		setEditingTermId(term.id);
								            		setTermNameDraft(term.name);
								            	}}
								            >
								            	{term.name}
								            </h3>
							            )}
                                        {/* <h3 className="font-medium border-b mb-2">{term.name}</h3> */}
                                        <div className="bg-white border p-2 rounded relative flex flex-col items-center">
                                            <div className="space-y-1">
                                                {/* Course / Block Container*/}
                                                {term.blocks.map((block) => (
                                                    <div
                                                        key={block.id}
                                                        className={`p-1 rounded text-sm ${
                                                            block.type === "course" ? "bg-pink-300" : "bg-blue-300"
                                                        }`}
                                                    >
                                                        {block.name}
                                                    </div>
                                                ))}
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
														onClick={() => addBlockToTerm(year.id, term.id, "course")}
													>
														Add Course
													</button>
													<button
														className="block w-full text-left px-4 py-2 hover:bg-gray-100"
														onClick={() => addBlockToTerm(year.id, term.id, "block")}
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
