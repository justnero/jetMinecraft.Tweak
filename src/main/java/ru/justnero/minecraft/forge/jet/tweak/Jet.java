package ru.justnero.minecraft.forge.jet.tweak;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.util.zip.ZipFile;

import cpw.mods.fml.common.FMLLog;

import sun.misc.URLClassPath;

import net.minecraft.client.Minecraft;

public class Jet {

    private static File gameDir;
    private static int gameID = 1;
    private static String title = "jetMinecraft";
    private static File jarLocation;

    public static final String auth() {
        // localhost
        // return new String(new byte[]{108,111,99,97,108,104,111,115,116});
        // justnero.ru
        // return new String(new byte[]{106,117,115,116,110,101,114,111,46,114,117});
        // auth.omsk-craft.ru
        // return new String(new byte[]{97,117,116,104,46,111,109,115,107,45,99,114,97,102,116,46,114,117});
        // auth.lz-craft.ru
        return new String(new byte[]{97,117,116,104,46,108,122,45,99,114,97,102,116,46,114,117});
    }

    public static final String urlSite() {
        // http://omsk-craft.ru
        // return new String(new byte[]{104,116,116,112,58,47,47,111,109,115,107,45,99,114,97,102,116,46,114,117});
        // http://lz-craft.ru
        return new String(new byte[]{104,116,116,112,58,47,47,108,122,45,99,114,97,102,116,46,114,117});
    }

    public static final String urlForum() {
        // http://omsk-craft.ru/forum
        // return new String(new byte[]{104,116,116,112,58,47,47,111,109,115,107,45,99,114,97,102,116,46,114,117,47,102,111,114,117,109});
        // http://lz-craft.ru/forum
        return new String(new byte[]{104,116,116,112,58,47,47,108,122,45,99,114,97,102,116,46,114,117,47,102,111,114,117,109});
    }

    public static final String urlVK() {
        // https://vk.com/omsk.craft
        // return new String(new byte[]{104,116,116,112,58,47,47,118,107,46,99,111,109,47,111,109,115,107,46,99,114,97,102,116});
        // https://vk.com/serverlzcraft
        return new String(new byte[]{104,116,116,112,58,47,47,118,107,46,99,111,109,47,115,101,114,118,101,114,108,122,99,114,97,102,116});
    }

    public static final String urlDeveloper() {
        // https://justnero.ru
        return new String(new byte[]{104,116,116,112,58,47,47,106,117,115,116,110,101,114,111,46,114,117});
    }

    public static final String project() {
        // jetMinecraft
        // return new String(new byte[]{106,101,116,77,105,110,101,99,114,97,102,116});
        // OMSK-Craft
        // return new String(new byte[]{79,77,83,75,45,67,114,97,102,116});
        // LZ-Craft
        return new String(new byte[]{76,90,45,67,114,97,102,116});
    }

    public static final String skins() {
        // http://omsk-craft.ru/sa/skin/%s.png
        // return new String(new byte[]{104,116,116,112,58,47,47,111,109,115,107,45,99,114,97,102,116,46,114,117,47,115,97,47,115,107,105,110,47,37,115,46,112,110,103});
        // http://lz-craft.ru/public/skins/plain/%s.png
        return new String(new byte[]{104,116,116,112,58,47,47,108,122,45,99,114,97,102,116,46,114,117,47,112,117,98,108,105,99,47,115,107,105,110,115,47,112,108,97,105,110,47,37,115,46,112,110,103});
    }

    public static final String cloaks() {
        // http://omsk-craft.ru/sa/skin/cloak/%s.png
        // return new String(new byte[]{104,116,116,112,58,47,47,111,109,115,107,45,99,114,97,102,116,46,114,117,47,115,97,47,115,107,105,110,47,99,108,111,97,107,47,37,115,46,112,110,103});
        // http://lz-craft.ru/sa/skin/cloak/%s.png
        return new String(new byte[]{104,116,116,112,58,47,47,108,122,45,99,114,97,102,116,46,114,117,47,115,97,47,115,107,105,110,47,99,108,111,97,107,47,37,115,46,112,110,103});
    }

    public static File direcotry() {
    	return gameDir == null ? gameDir = genDirectory() : gameDir;
    }

    private static File genDirectory() {
        try {
            String tmp = Jet.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            tmp = tmp.substring(10,tmp.length()-11);
            return (new File(tmp)).getParentFile().getParentFile();
        } catch(URISyntaxException ex) {
            FMLLog.getLogger().catching(ex);
            Minecraft.getMinecraft().shutdown();
            return null;
        }
    }

    public static File jarLocation() {
    	return jarLocation == null ? jarLocation = genJarLocation() : jarLocation;
    }

    private static File genJarLocation() {
        try {
            String tmp = Jet.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            tmp = tmp.substring(10,tmp.length()-11);
            return new File(tmp);
        } catch(URISyntaxException ex) {
            FMLLog.getLogger().catching(ex);
            Minecraft.getMinecraft().shutdown();
            return null;
        }
    }

    private static ArrayList<String> hashes(File d) {
        ArrayList<String> tmp_map = new ArrayList<String>();
        if(d.listFiles() != null) {
	        for(File f : d.listFiles()) {
	            if(f.isDirectory()) {
	                tmp_map.addAll(hashes(f));
	            } else {
	                if(f.getName().endsWith(".jar") || f.getName().endsWith(".zip") || f.getName().endsWith(".class")) {
	                    tmp_map.add(sha1(f));
	                }
	            }
	        }
        }
        return tmp_map;
    }

    private static String sha1(File file) {
        StringBuilder hexString = new StringBuilder();
        MessageDigest md;
        FileInputStream fis = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            for(int i=0;i<mdbytes.length;i++) {
                String hex = Integer.toHexString(0xff & mdbytes[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            fis.close();
        } catch (Exception e) {
            try {
                fis.close();
            } catch (Exception ex) {}
            return e.toString();
        }
        return hexString.toString();
    }

    private static String sha1(String str) {
        return sha1(str.getBytes());
    }

    private static String sha1(byte[] str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch(NoSuchAlgorithmException e) {
            return e.toString();
        }
        return hex(md.digest(str));
    }

    private static String hex(byte[] byteData) {
        StringBuilder  hexString = new StringBuilder();
        for(int i=0;i<byteData.length;i++)
            hexString.append(Integer.toString((byteData[i]&0xff)+0x100,16).substring(1));
        return hexString.toString();
    }

    private static Socket socket() {
        try {
            return new Socket(InetAddress.getByName(auth()),15705);
        } catch(IOException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    public static boolean checkServer(String userName, String server) throws IOException {
    	Socket socket = socket();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(10);
        out.writeUTF(userName);
        out.writeUTF(server);
        out.flush();
        int ret_val = in.readInt();
        in.close();
        out.close();
        socket.close();
        return ret_val == 200;
    }

    public static String joinServer(String userName, String sessionID, String server, int serverID) throws IOException {
        Socket socket = socket();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        ArrayList<String> hashes = hashes(genDirectory());
        String[] hashList = hashes.toArray(new String[hashes.size()]);
        Arrays.sort(hashList);
        StringBuilder sb = new StringBuilder();
        for(String temp : hashList) {
            sb.append(temp);
        }
        String hash = sha1(sb.toString());
        out.writeInt(9);
        out.writeUTF(userName);
        out.writeUTF(sha1(sessionID+server));
        out.writeUTF(server);
        out.writeInt(serverID);
        out.writeUTF(hash);
        out.flush();
        String ret_val = in.readUTF();
        in.close();
        out.close();
        socket.close();
        return ret_val;
    }

    private static String authServer(String username, String sessionID, Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(7);
        out.writeUTF(username);
        // out.writeUTF(jetSHA1(jetSHA1(sessionID+jetSHA1("Get away from here!"))));
        String pepper = sha1(new String(new byte[]{71,101,116,32,97,119,97,121,32,102,114,111,109,32,104,101,114,101,33}));
        out.writeUTF(sha1(sha1(sessionID+pepper)));
        out.flush();
        int code = in.readInt();
        String ret_val = code == 200 ? in.readUTF() : null;
        in.close();
        out.close();
        socket.close();
        return ret_val;
    }

    @SuppressWarnings("unchecked")
	public static String authServer(String username, String sessionID) {
        try {
            URL[] urls = new URL[]{new File(genDirectory(),new String(new byte[]{47,98,105,110,47,109,105,110,101,99,114,97,102,116,46,106,97,114})).toURI().toURL()};
            URLClassPath urlcp = new URLClassPath(urls,null);
            Field field = urlcp.getClass().getDeclaredField(new String(new byte[]{112,97,116,104}));
            field.setAccessible(true);
            ArrayList<URL> list = (ArrayList<URL>) field.get(urlcp);
            if(list.size() == urls.length) {
                for(Method m : FileInputStream.class.getMethods()) {
                    if(m.getName().equalsIgnoreCase(new String(new byte[]{114,101,97,100,70,105,108,101}))) {
                        return null;
                    }
                }
                URL url = ZipFile.class.getResource(new String(new byte[]{47,106,97,118,97,47,117,116,105,108,47,122,105,112,47,90,105,112,70,105,108,101,46,99,108,97,115,115}));
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNextLine()) {
                	String line = sc.nextLine();
                    if(line.contains(new String(new byte[]{106,97,118,97,47,105,111,47,66,117,102,102,101,114,101,100,73,110,112,117,116,83,116,114,101,97,109})) ||
                	   line.contains(new String(new byte[]{114,101,112,108,97,99,101}))) {
                    	sc.close();
                    	return null;
                    }
                }
                sc.close();
                try {
                    String val1 = WinRegistry.readString(
                        WinRegistry.HKEY_CURRENT_USER,
                        new String(new byte[]{83,79,70,84,87,65,82,69,92,92,77,105,99,114,111,115,111,102,116,92,92,87,105,110,100,111,119,115,32,78,84,92,92,67,117,114,114,101,110,116,86,101,114,115,105,111,110,92,92,87,105,110,100,111,119,115}), // SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Windows
                        new String(new byte[]{65,112,112,73,110,105,116,95,68,76,76,115})); // AppInit_DLLs
                    String val2 = WinRegistry.readString(
                        WinRegistry.HKEY_LOCAL_MACHINE,
                        new String(new byte[]{83,79,70,84,87,65,82,69,92,92,77,105,99,114,111,115,111,102,116,92,92,87,105,110,100,111,119,115,32,78,84,92,92,67,117,114,114,101,110,116,86,101,114,115,105,111,110,92,92,87,105,110,100,111,119,115}), // SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Windows
                        new String(new byte[]{65,112,112,73,110,105,116,95,68,76,76,115})); // AppInit_DLLs
                    if(!((val1 == null || val1.equals(new String(new byte[]{110,111})) || val1.isEmpty()) &&
                         (val2 == null || val2.equals(new String(new byte[]{110,111})) || val2.isEmpty()))) { // no
                        return null;
                    }						   
                } catch(Exception ex) {
                        return null;
                }
                return authServer(username,sessionID,socket());
            }
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }

    public static List<JetServer> listServer(int gameID) throws IOException {
        Socket socket = socket();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(8);
        out.writeInt(gameID);
        out.flush();
        if(in.readInt() == 200) {
            int cnt = in.readInt();
            List<JetServer> list = new ArrayList<JetServer>(cnt);
            for(int i=0;i<cnt;i++) {
                list.add(new JetServer(in.readInt(),in.readUTF(),in.readUTF()));
            }
            return list;
        } else {
            throw new IOException();
        }
    }

    public static int gameID() {
    	return gameID;
    }

    public static void setGameID(int game) {
    	if(gameID != 0) {
    		gameID = game;
    	}
    }

    public static void setTitle(String name) {
    	title = name;
    }

    public static String title() {
    	return title;
    }

    private static class WinRegistry {
        public static final int HKEY_CURRENT_USER = 0x80000001;
        public static final int HKEY_LOCAL_MACHINE = 0x80000002;
        public static final int REG_SUCCESS = 0;
        public static final int REG_NOTFOUND = 2;
        public static final int REG_ACCESSDENIED = 5;

        private static final int KEY_ALL_ACCESS = 0xf003f;
        private static final int KEY_READ = 0x20019;
        private static Preferences userRoot = Preferences.userRoot();
        private static Preferences systemRoot = Preferences.systemRoot();
        private static Class<? extends Preferences> userClass = userRoot.getClass();
        private static Method regOpenKey = null;
        private static Method regCloseKey = null;
        private static Method regQueryValueEx = null;
        private static Method regEnumValue = null;
        private static Method regQueryInfoKey = null;
        private static Method regEnumKeyEx = null;
        private static Method regCreateKeyEx = null;
        private static Method regSetValueEx = null;
        private static Method regDeleteKey = null;
        private static Method regDeleteValue = null;

        static {
            try {
                regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey",
                    new Class[] { int.class, byte[].class, int.class });
                regOpenKey.setAccessible(true);
                regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey",
                    new Class[] { int.class });
                regCloseKey.setAccessible(true);
                regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx",
                    new Class[] { int.class, byte[].class });
                regQueryValueEx.setAccessible(true);
                regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue",
                    new Class[] { int.class, int.class, int.class });
                regEnumValue.setAccessible(true);
                regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1",
                    new Class[] { int.class });
                regQueryInfoKey.setAccessible(true);
                regEnumKeyEx = userClass.getDeclaredMethod(  
                    "WindowsRegEnumKeyEx", new Class[] { int.class, int.class,  
                        int.class });  
                regEnumKeyEx.setAccessible(true);
                regCreateKeyEx = userClass.getDeclaredMethod(  
                    "WindowsRegCreateKeyEx", new Class[] { int.class,  
                        byte[].class });  
                regCreateKeyEx.setAccessible(true);  
                regSetValueEx = userClass.getDeclaredMethod(  
                    "WindowsRegSetValueEx", new Class[] { int.class,  
                        byte[].class, byte[].class });  
                regSetValueEx.setAccessible(true); 
                regDeleteValue = userClass.getDeclaredMethod(  
                    "WindowsRegDeleteValue", new Class[] { int.class,  
                        byte[].class });  
                regDeleteValue.setAccessible(true); 
                regDeleteKey = userClass.getDeclaredMethod(  
                    "WindowsRegDeleteKey", new Class[] { int.class,  
                        byte[].class });  
                regDeleteKey.setAccessible(true); 
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        private WinRegistry() {}

        /**
         * Read a value from key and value name
         * @param hkey   HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
         * @param key
         * @param valueName
         * @return the value
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static String readString(int hkey, String key, String valueName) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            if(hkey == HKEY_LOCAL_MACHINE) {
                return readString(systemRoot, hkey, key, valueName);
            } else if(hkey == HKEY_CURRENT_USER) {
                return readString(userRoot, hkey, key, valueName);
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
        }

        /**
         * Read value(s) and value name(s) form given key 
         * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
         * @param key
         * @return the value name(s) plus the value(s)
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static Map<String, String> readStringValues(int hkey, String key) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            if(hkey == HKEY_LOCAL_MACHINE) {
                return readStringValues(systemRoot, hkey, key);
            } else if(hkey == HKEY_CURRENT_USER) {
                return readStringValues(userRoot, hkey, key);
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
        }

        /**
         * Read the value name(s) from a given key
         * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
         * @param key
         * @return the value name(s)
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static List<String> readStringSubKeys(int hkey, String key) 
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
            if(hkey == HKEY_LOCAL_MACHINE) {
                return readStringSubKeys(systemRoot, hkey, key);
            } else if(hkey == HKEY_CURRENT_USER) {
                return readStringSubKeys(userRoot, hkey, key);
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
        }

        /**
         * Create a key
         * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
         * @param key
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static void createKey(int hkey, String key) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int [] ret;
            if(hkey == HKEY_LOCAL_MACHINE) {
                ret = createKey(systemRoot, hkey, key);
                regCloseKey.invoke(systemRoot, new Object[] { new Integer(ret[0]) });
            } else if(hkey == HKEY_CURRENT_USER) {
                ret = createKey(userRoot, hkey, key);
                regCloseKey.invoke(userRoot, new Object[] { new Integer(ret[0]) });
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
            if(ret[1] != REG_SUCCESS) {
                throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
            }
        }

        /**
         * Write a value in a given key/value name
         * @param hkey
         * @param key
         * @param valueName
         * @param value
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static void writeStringValue
          (int hkey, String key, String valueName, String value) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            if(hkey == HKEY_LOCAL_MACHINE) {
                writeStringValue(systemRoot, hkey, key, valueName, value);
            } else if(hkey == HKEY_CURRENT_USER) {
                writeStringValue(userRoot, hkey, key, valueName, value);
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
        }

        /**
         * Delete a given key
         * @param hkey
         * @param key
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static void deleteKey(int hkey, String key) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int rc = -1;
            if(hkey == HKEY_LOCAL_MACHINE) {
                rc = deleteKey(systemRoot, hkey, key);
            } else if(hkey == HKEY_CURRENT_USER) {
                rc = deleteKey(userRoot, hkey, key);
            }
            if(rc != REG_SUCCESS) {
                throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
            }
        }

        /**
         * delete a value from a given key/value name
         * @param hkey
         * @param key
         * @param value
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public static void deleteValue(int hkey, String key, String value) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int rc = -1;
            if(hkey == HKEY_LOCAL_MACHINE) {
                rc = deleteValue(systemRoot, hkey, key, value);
            } else if(hkey == HKEY_CURRENT_USER) {
                rc = deleteValue(userRoot, hkey, key, value);
            }
            if(rc != REG_SUCCESS) {
                throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
            }
        }

        // =====================

        private static int deleteValue
          (Preferences root, int hkey, String key, String value)
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
                new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS) });
            if(handles[1] != REG_SUCCESS) {
                return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
            }
            int rc =((Integer) regDeleteValue.invoke(root,  
                new Object[] { 
                  new Integer(handles[0]), toCstr(value) 
                  })).intValue();
            regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
            return rc;
        }

        private static int deleteKey(Preferences root, int hkey, String key) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int rc =((Integer) regDeleteKey.invoke(root,  
                new Object[] { new Integer(hkey), toCstr(key) })).intValue();
            return rc;  // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
        }

        private static String readString(Preferences root, int hkey, String key, String value)
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
                new Integer(hkey), toCstr(key), new Integer(KEY_READ) });
            if(handles[1] != REG_SUCCESS) {
                return null; 
            }
            byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[] {
                new Integer(handles[0]), toCstr(value) });
            regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
            return (valb != null ? new String(valb).trim() : null);
        }

        private static Map<String,String> readStringValues
          (Preferences root, int hkey, String key)
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            HashMap<String, String> results = new HashMap<String,String>();
            int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
                new Integer(hkey), toCstr(key), new Integer(KEY_READ) });
            if(handles[1] != REG_SUCCESS) {
                return null;
            }
            int[] info = (int[]) regQueryInfoKey.invoke(root,
                new Object[] { new Integer(handles[0]) });

            int count = info[0]; // count  
            int maxlen = info[3]; // value length max
            for(int index=0; index<count; index++) {
                byte[] name = (byte[]) regEnumValue.invoke(root, new Object[] {
                    new Integer
                      (handles[0]), new Integer(index), new Integer(maxlen + 1)});
                String value = readString(hkey, key, new String(name));
                results.put(new String(name).trim(), value);
            }
            regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
            return results;
        }

        private static List<String> readStringSubKeys
          (Preferences root, int hkey, String key)
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            List<String> results = new ArrayList<String>();
            int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
                new Integer(hkey), toCstr(key), new Integer(KEY_READ) 
                });
            if(handles[1] != REG_SUCCESS) {
                return null;
            }
            int[] info = (int[]) regQueryInfoKey.invoke(root,
                new Object[] { new Integer(handles[0]) });

            int count  = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
            int maxlen = info[3]; // value length max
            for(int index=0; index<count; index++) {
                byte[] name = (byte[]) regEnumKeyEx.invoke(root, new Object[] {
                    new Integer
                      (handles[0]), new Integer(index), new Integer(maxlen + 1)
                    });
                results.add(new String(name).trim());
            }
            regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
            return results;
        }

        private static int [] createKey(Preferences root, int hkey, String key)
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            return  (int[]) regCreateKeyEx.invoke(root,
                new Object[] { new Integer(hkey), toCstr(key) });
        }

        private static void writeStringValue 
          (Preferences root, int hkey, String key, String valueName, String value) 
          throws IllegalArgumentException, IllegalAccessException,
          InvocationTargetException {
            int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
                new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS) });

            regSetValueEx.invoke(root,  
                new Object[] { 
                  new Integer(handles[0]), toCstr(valueName), toCstr(value) 
                  }); 
            regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
        }

        // utility
        private static byte[] toCstr(String str) {
            byte[] result = new byte[str.length() + 1];

            for (int i = 0; i < str.length(); i++) {
                result[i] = (byte) str.charAt(i);
            }
            result[str.length()] = 0;
            return result;
        }
    }
}