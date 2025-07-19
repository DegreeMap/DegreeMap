import React from "react"

interface CourseCardModalProps {
	block: {
        title: string
        code: string
        credits: number
    }
    isOpen: boolean
    onClose: () => void
    onSave: (updated: { title: string; code: string; credits: number }) => void;
}

export const CourseCardModal: React.FC<CourseCardModalProps>= ({ block, isOpen, onClose, onSave }) => {
    return (
        <div></div>
    )
}