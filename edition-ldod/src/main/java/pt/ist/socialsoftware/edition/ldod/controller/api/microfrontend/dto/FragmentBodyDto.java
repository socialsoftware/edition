package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.ist.socialsoftware.edition.ldod.domain.AppText;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;
import pt.ist.socialsoftware.edition.ldod.domain.Surface;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;

public class FragmentBodyDto {

	private FragmentDto fragment;
	private LdoDDto LdoD;
	private LdoDUserDto LdoDuser;
	private List<FragInterDto> inters;
	private boolean hasAccess;
	private String transcript;
	private List<VirtualEditionDto> virtualEditionsDto;
	private List<String> setTranscriptionSideBySide;
	private List<SurfaceDto> surfaces;
	private ArrayList<ArrayList<String>> variations;
	private String writerLineByLine;
	private SurfaceDto prevSurface;
	private SurfaceDto nextSurface;
	private String prevPb;
	private String nextPb;
	private SurfaceDto surface;
	
	
	public FragmentBodyDto(LdoD instance, LdoDUser user, Fragment fragment, ArrayList<FragInter> inters) {
		this.setFragment(new FragmentDto(fragment));
		this.setLdoD(new LdoDDto(instance, fragment, user, inters));
		if(user!=null) {
			this.setLdoDuser(new LdoDUserDto(user));
		}
		this.setInters(inters.stream().map(FragInterDto::new).collect(Collectors.toList()));
	}

	public FragmentBodyDto(LdoD instance, LdoDUser user,
			Fragment fragment, List<FragInter> inters, PlainHtmlWriter4OneInter writer, boolean hasAccess, ArrayList<String> selectedVEAcr) {
		this.setFragment(new FragmentDto(fragment));
		this.setLdoD(new LdoDDto(instance, fragment, user, (ArrayList<FragInter>) inters));
		if(user!=null) {
			this.setLdoDuser(new LdoDUserDto(user));
		}
		this.setInters(inters.stream().map(FragInterDto::new).collect(Collectors.toList()));
		this.setHasAccess(hasAccess);
		if(writer != null) {
			this.setTranscript(writer.getTranscription());
		}
		

		List<VirtualEdition> virtualEditions = selectedVEAcr.stream().map(acr -> instance.getEdition(acr)).filter(e -> e != null)
				.map(VirtualEdition.class::cast).collect(Collectors.toList());
		
		this.setVirtualEditionsDto(virtualEditions.stream().map(vEdition -> new VirtualEditionDto(vEdition, fragment, user, inters.get(0))).collect(Collectors.toList()));
	}

	public FragmentBodyDto(LdoD instance, LdoDUser user,
			Fragment fragment, List<FragInter> inters, HtmlWriter2CompInters writer,
			PlainHtmlWriter4OneInter writer4One, Map<FragInter, HtmlWriter4Variations> variations, List<AppText> apps) {
		
		this.setFragment(new FragmentDto(fragment));
		this.setLdoD(new LdoDDto(instance, fragment, user, (ArrayList<FragInter>) inters));
		if(user!=null) {
			this.setLdoDuser(new LdoDUserDto(user));
		}
		this.setInters(inters.stream().map(FragInterDto::new).collect(Collectors.toList()));
		this.setHasAccess(hasAccess);
		if(writer != null) {
			if(inters.size() > 1) {
				this.setSetTranscriptionSideBySide(inters.stream().map(fragInter -> writer.getTranscription(fragInter)).collect(Collectors.toList()));
			}
			if(inters.size() > 2) {
				this.setWriterLineByLine(writer.getTranscriptionLineByLine());
			}
			
		}
		if(writer4One != null) {
			this.setTranscript(writer4One.getTranscription());
		}
		
		ArrayList<ArrayList<String>> variationsTranscriptions = new ArrayList<ArrayList<String>>();
		ArrayList<String> array = new ArrayList<String>();
		
		
		System.out.println(apps.size());
		
		
		for(int i = 0; i < apps.size(); i++) {
			array = new ArrayList<String>();
			for(FragInter interp: inters) {
				array.add(variations.get(interp).getAppTranscription(apps.get(i)));
			}
			variationsTranscriptions.add(array);
		}
		this.setVariations(variationsTranscriptions);
		
	}

	public FragmentBodyDto(List<FragInter> inters, PlainHtmlWriter4OneInter writer4One) {
		if(writer4One != null) {
			this.setTranscript(writer4One.getTranscription());
		}
		this.setInters(inters.stream().map(FragInterDto::new).collect(Collectors.toList()));
	}

	public FragmentBodyDto(List<FragInter> inters, Surface surface, PbText pbText, PlainHtmlWriter4OneInter writer4One, SourceInter inter) {
		if(writer4One != null) {
			this.setTranscript(writer4One.getTranscription());
		}
		this.setInters(inters.stream().map(FragInterDto::new).collect(Collectors.toList()));
		this.setSurface(new SurfaceDto(surface));
		
		if(inter.getPrevSurface(pbText) != null) {
			this.setPrevSurface(new SurfaceDto(inter.getPrevSurface(pbText)));
		}
		System.out.println(inter.getPrevPbText(pbText));
		if(inter.getNextSurface(pbText) != null) {
			this.setNextSurface(new SurfaceDto(inter.getNextSurface(pbText)));
		}
		if(inter.getPrevPbText(pbText) != null) {
			this.setPrevPb(inter.getPrevPbText(pbText).getExternalId());
		}
		if(inter.getNextPbText(pbText) != null) {
			this.setNextPb(inter.getNextPbText(pbText).getExternalId());
		}
		
		
				
	}

	public FragmentBodyDto(List<FragInter> inters, HtmlWriter2CompInters writer) {
		this.setInters(inters.stream().map(FragInterDto::new).collect(Collectors.toList()));
		this.setWriterLineByLine(writer.getTranscriptionLineByLine());
		System.out.println(writer.getTranscriptionLineByLine());
	}

	public FragmentDto getFragment() {
		return fragment;
	}

	public void setFragment(FragmentDto fragment) {
		this.fragment = fragment;
	}

	public LdoDDto getLdoD() {
		return LdoD;
	}

	public void setLdoD(LdoDDto ldoD) {
		LdoD = ldoD;
	}

	public LdoDUserDto getLdoDuser() {
		return LdoDuser;
	}

	public void setLdoDuser(LdoDUserDto ldoDuser) {
		LdoDuser = ldoDuser;
	}

	public List<FragInterDto> getInters() {
		return inters;
	}

	public void setInters(List<FragInterDto> inters) {
		this.inters = inters;
	}

	public boolean isHasAccess() {
		return hasAccess;
	}

	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}
	
	public String getTranscript() {
		return transcript;
	}


	public void setTranscript(String transcript) {
		this.transcript = transcript;
	}

	public List<VirtualEditionDto> getVirtualEditionsDto() {
		return virtualEditionsDto;
	}

	public void setVirtualEditionsDto(List<VirtualEditionDto> virtualEditionsDto) {
		this.virtualEditionsDto = virtualEditionsDto;
	}

	public List<String> getSetTranscriptionSideBySide() {
		return setTranscriptionSideBySide;
	}

	public void setSetTranscriptionSideBySide(List<String> setTranscriptionSideBySide) {
		this.setTranscriptionSideBySide = setTranscriptionSideBySide;
	}

	public List<SurfaceDto> getSurfaces() {
		return surfaces;
	}

	public void setSurfaces(List<SurfaceDto> surfaces) {
		this.surfaces = surfaces;
	}

	public ArrayList<ArrayList<String>> getVariations() {
		return variations;
	}

	public void setVariations(ArrayList<ArrayList<String>> variationsTranscriptions) {
		this.variations = variationsTranscriptions;
	}

	public String getWriterLineByLine() {
		return writerLineByLine;
	}

	public void setWriterLineByLine(String writerLineByLine) {
		this.writerLineByLine = writerLineByLine;
	}

	public SurfaceDto getPrevSurface() {
		return prevSurface;
	}

	public void setPrevSurface(SurfaceDto prevSurface) {
		this.prevSurface = prevSurface;
	}

	public SurfaceDto getNextSurface() {
		return nextSurface;
	}

	public void setNextSurface(SurfaceDto nextSurface) {
		this.nextSurface = nextSurface;
	}

	public String getPrevPb() {
		return prevPb;
	}

	public void setPrevPb(String prevPb) {
		this.prevPb = prevPb;
	}

	public String getNextPb() {
		return nextPb;
	}

	public void setNextPb(String nextPb) {
		this.nextPb = nextPb;
	}

	public SurfaceDto getSurface() {
		return surface;
	}

	public void setSurface(SurfaceDto surface) {
		this.surface = surface;
	}

}
