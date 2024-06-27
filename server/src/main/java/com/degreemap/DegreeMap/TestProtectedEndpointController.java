package com.degreemap.DegreeMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestProtectedEndpointController {

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("clandestine")
    public ResponseEntity<?> getVeryVerySecretThingThatNo1ShouldKnowAbout(Principal principal) {
        return ResponseEntity.ok(
                "Item #: SCP-914\n" +
                "\n" +
                "Object Class: Safe\n" +
                "\n" +
                "Special Containment Procedures: Only personnel who submit a formal request and receive approval from site command may operate 914. SCP-914 is to be kept in research cell 109-B with two guard personnel on duty at all times. Any researchers entering 109-B are to be accompanied by at least one guard for the entirety of testing. A full list of tests to be carried out must be given to all guard personnel on duty; any deviation from this list will result in termination of testing, forcible removal of personnel from 109-B, and formal discipline at site command’s discretion.\n" +
                "\n" +
                "WARNING: At this time, no testing of biological matter is allowed. Refer to document 109-B:117. Applying the “Rough” setting to explosive materials is not advised.\n" +
                "\n" +
                "Description: SCP-914 is a large clockwork device weighing several tons and covering an area of eighteen square meters, consisting of screw drives, belts, pulleys, gears, springs and other clockwork. It is incredibly complex, consisting of over eight million moving parts comprised mostly of tin and copper, with some wooden and cloth items observed. Observation and probing have showed no electronic assemblies or any form of power other than the “Mainspring” under the “Selection Panel”. Two large booths 3mx2.1mx2.1m (10ftx7ftx7ft) are connected via copper tubes to the main body of SCP-914, labeled “Intake” and “Output”. Between them is a copper panel with a large knob with a small arrow attached. The words Rough, Coarse, 1:1, Fine, and Very Fine are positioned at points around the knob. Below the knob is a large “key” that winds the “mainspring”.\n" +
                "\n" +
                "When an object is placed in the Intake Booth, a door slides shut, and a small bell sounds. If the knob is turned to any position and the key wound up, SCP-914 will “refine” the object in the booth. No energy is lost in the process, and the object appears to be in stasis until the Output Booth door is opened. Intense observation and testing have not shown how SCP-914 accomplishes this, and no test object has ever been observed inside SCP-914 during the “refining” process. The process takes between five and ten minutes, depending on the size of the object being refined.\n" +
                "\n" +
                "Addendum: 5/14: Dr. █████ Test Log\n" +
                "\n" +
                "Input: 1kg of steel (setting: Rough)\n" +
                "\n" +
                "Output: Pile of steel chunks of various sizes, appearing to be cut by laser.\n" +
                "\n" +
                "Input: 1kg of steel (setting: 1:1)\n" +
                "\n" +
                "Output: 1kg of steel screws\n" +
                "\n" +
                "Input: 1kg of steel (setting: Fine)\n" +
                "\n" +
                "Output: 1kg of steel carpet tacks\n" +
                "\n" +
                "Input: 1kg of steel (setting: Very Fine)\n" +
                "\n" +
                "Output: Several gases that dissipated into the air quickly, and 1 gram of an unknown metal, resistant to heat of 50,000 degrees, impossible to bend or break with any force, and a near-perfect (1.6x10-75 ρ) conductor of electricity\n" +
                "\n" +
                "Input: 1 wristwatch belonging to Dr. █████ (setting: Coarse)\n" +
                "\n" +
                "Output: 1 completely disassembled wristwatch\n" +
                "\n" +
                "Input: 1 cellphone belonging to ███████ (setting: 1:1)\n" +
                "\n" +
                "Output: 1 cellphone, although different make and model\n" +
                "\n" +
                "Input: 1 standard Colt Python revolver (setting: Very Fine)\n" +
                "\n" +
                "Output: [DATA EXPUNGED] Aforementioned ████████████ completely disintegrated all matter in its line of fire. Object contained with high density gamma waves.\n" +
                "\n" +
                "Input: 1 white mouse (setting: 1:1)\n" +
                "\n" +
                "Output: 1 brown mouse\n" +
                "\n" +
                "Input: 1 chimp (setting: Fine)\n" +
                "\n" +
                "Output: [DATA EXPUNGED]\n" +
                "\n" +
                "Input: 1 chimp (setting: Rough)\n" +
                "\n" +
                "Output: Badly mutilated corpse, showing signs of crushing and cutting with high heat\n" +
                "\n" +
                "Document # 109-B:117: Dr.███ and Dr.███████ Test Log\n" +
                "\n" +
                "Input: Subject D-186, male Caucasian, 42 years old, 108kg, 185cm tall. (setting: 1:1)\n" +
                "\n" +
                "Output: Male Hispanic, 42 years old, 100kg, 188cm tall. Subject was very confused and agitated. Subject attacked security personnel. Subject terminated.\n" +
                "\n" +
                "Input: Subject D-187, male Caucasian, 28 years old, 63kg, 173cm tall. (setting: Very Fine)\n" +
                "\n" +
                "Output: [DATA EXPUNGED]. Subject escaped from test chamber, killing eight guards as well as Dr.███ and Dr.███████. Lockdown initiated. Subject causes containment failure of three SCP areas in continued escape attempt. Special response team engages subject, resulting in severe wounding of subject, partial memory loss in special response team members and corrosive damage to plumbing. Subject expired several hours later, dissolving into blue ash and blinding nearby research team.\n" +
                "\n" +
                "Biological testing with SCP-914 discontinued.\n" +
                "\n" +
                "Note: \"Because of the nature of this SCP a wide range of test data would be helpful. Dr. Gears has ordered that any researcher can have access for non-biological testing if they themselves are or they are supervised by a Level 3 researcher. All testing is to be recorded in file #914-E (Experiment Log 914). Biological testing will continue only with prior clearance by 05 Command. As long as you want to try something mundane that isn't alive feel free to help accumulate data.\" - Dr. █████");
    }
}
