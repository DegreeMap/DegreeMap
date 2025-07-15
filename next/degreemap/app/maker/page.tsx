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
	return (
		<div>
			<NavBar />
			<div className="p-4">
				<Button onClick={addYear}>add year</Button>
				<div className="mt-4 space-y-4 gap-4 flex">
					{years.map((year) => (
						<div key={year.id} className="border p-4 rounded-lg bg-gray-100">
							<h2 className="text-lg font-semibold mb-2">{year.name}</h2>
							<div className="flex gap-4">
                            {year.terms.map((term) => (
									<div key={term.id} className="bg-white border p-2 w-48 rounded relative">
										<h3 className="font-medium border-b mb-2">{term.name}</h3>
										<div className="space-y-1">
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
										<div className="mt-2 relative">
											<Button
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
												<div className="absolute z-10 mt-2 w-32 bg-white border rounded shadow">
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
								))}
							</div>
						</div>
					))}
				</div>
			</div>
		</div>
	);
}
