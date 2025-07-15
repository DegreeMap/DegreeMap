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
	};

	return (
		<div>
			<NavBar />
            <p>degree map maker!</p>
		</div>
	);
}
